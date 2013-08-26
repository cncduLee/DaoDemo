package com.lpm.fanger.search.base;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

import com.lpm.fanger.commons.util.Reflections;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-22
 */
public class LuceneIndexUtils {
	private final static String _g_lucene_path = "D:/lucene_idx/";

	private final static String _KEYWORD_FIELD_NAME = "id";
	private final static String _FMT_DATE = "yyyyMMddHHmmssSSS";
	private final static IKAnalyzer g_analyzer = new IKAnalyzer(true);

	public final static Analyzer getAnalyzer() {
		return g_analyzer;
	}

	/**
	 * 添加文档
	 * 
	 * @param objClass
	 * @param doc
	 * @throws Exception
	 */
	public static int add(Class<? extends SearchEnabled> objClass,
			List<? extends SearchEnabled> docs) throws Exception {
		if (docs == null || docs.size() == 0)
			return 0;
		IndexWriter writer = _GetWriter(objClass);
		try {
			int ar = _Add(writer, docs);
			writer.optimize();
			return ar;
		} finally {
			writer.close();
			writer = null;
		}
	}

	/**
	 * 添加文档
	 * 
	 * @param doc
	 * @throws Exception
	 */
	private static int _Add(IndexWriter writer,
			List<? extends SearchEnabled> docs) throws Exception {
		if (docs == null || docs.size() == 0)
			return 0;
		int doc_count = 0;
		for (SearchEnabled doc : docs) {
			Document lucene_doc = _ObjectToDocument(doc);
			lucene_doc.setBoost(doc.GetBoost());
			writer.addDocument(lucene_doc);
			doc_count++;
		}
		return doc_count;
	}

	/**
	 * 从索引库中搜索
	 * 
	 * @param beanClass 操作对象类
	 * @param query 查询语句
	 * @param max_count 最大结果集数量
	 * @return
	 * @throws IOException
	 */
	public static List<Long> find(Class<? extends SearchEnabled> beanClass,
			Query query, int max_count) throws IOException {
		IndexSearcher searcher = _GetSearcher(beanClass);
		try {
			TopDocs hits = searcher.search(query, null, max_count);
			if (hits == null)
				return null;
			List<Long> results = new ArrayList<Long>();
			int numResults = Math.min(hits.totalHits, max_count);
			for (int i = 0; i < numResults; i++) {
				ScoreDoc s_doc = (ScoreDoc) hits.scoreDocs[i];
				Document doc = searcher.doc(s_doc.doc);
				long id = NumberUtils.toLong(doc.get(_KEYWORD_FIELD_NAME), 0);
				if (id > 0 && !results.contains(id))
					results.add(id);
			}
			return results;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			searcher.close();
		}
	}
	
	public static List<? extends SearchEnabled> find(
			Class<? extends SearchEnabled> beanClass,
			Query query,int max_count,boolean isTest) throws IOException {
		
		IndexSearcher searcher = _GetSearcher(beanClass);
		List results = new ArrayList();
		try {
			TopDocs hits = searcher.search(query, null, max_count);
			System.out.println("total results : "+hits.totalHits);
			if (hits == null)
				return null;
			int numResults = Math.min(hits.totalHits, max_count);
			for (int i = 0; i < numResults; i++) {
				ScoreDoc s_doc = (ScoreDoc) hits.scoreDocs[i];
				Document doc = searcher.doc(s_doc.doc);
				Object obj = _DocumentToObject(beanClass,doc);
				results.add(obj);
			}
			
			return results;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			searcher.close();
		}
		
		return results;
	}

	/**
	 * 添加文档
	 * 
	 * @param doc
	 * @throws Exception
	 */
	public static void add(SearchEnabled doc) throws Exception {
		if (doc == null)
			return;
		IndexWriter writer = _GetWriter(doc.getClass());
		try {
			writer.addDocument(_ObjectToDocument(doc));
			writer.commit();
		} finally {
			writer.close();
		}
	}

	public static void update(SearchEnabled doc) throws Exception {
		if (doc == null)
			return;
		IndexWriter writer = _GetWriter(doc.getClass());
		try {
			writer.deleteDocuments(new Term("id", String.valueOf(doc.getId())));
			writer.addDocument(_ObjectToDocument(doc));
			writer.commit();
		} finally {
			writer.close();
		}
	}

	public static void delete(SearchEnabled doc) throws IOException {
		if (doc == null)
			return;
		IndexWriter writer = _GetWriter(doc.getClass());
		try {
			writer.deleteDocuments(new Term("id", String.valueOf(doc.getId())));
			writer.commit();
		} finally {
			writer.close();
		}
	}

	/**
	 * 获取索引写
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	protected static IndexWriter _GetWriter(Class<?> beanClass)
			throws IOException {
		Directory indexDir = FSDirectory.open(new File(_g_lucene_path
				+ beanClass.getSimpleName()));
		return new IndexWriter(indexDir, g_analyzer,
				IndexWriter.MaxFieldLength.UNLIMITED);
	}

	/**
	 * 获取索引读
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	protected static IndexSearcher _GetSearcher(Class<?> beanClass)
			throws IOException {
		Directory indexDir = FSDirectory.open(new File(_g_lucene_path
				+ beanClass.getSimpleName()));
		IndexSearcher is = new IndexSearcher(indexDir);
		is.setSimilarity(new IKSimilarity());
		return is;
	}

	private final static NumberFormat _FMT_ID = NumberFormat.getInstance();
	
	static {
		_FMT_ID.setGroupingUsed(false);
		_FMT_ID.setMaximumFractionDigits(0);
		_FMT_ID.setMaximumIntegerDigits(12);
		_FMT_ID.setMinimumIntegerDigits(12);
	}

	private static Document _ObjectToDocument(SearchEnabled doc)
			throws Exception {
		Document lucene_doc = new Document();

		// Set keyword field
		lucene_doc.add(_Keyword(_KEYWORD_FIELD_NAME,_FMT_ID.format(doc.getId())));

		// Set storage field
		String[] storeFields = doc.GetStoreFields();
		if (storeFields != null)
			for (String s_field : storeFields) {
				String propertyValue = _GetField(doc, s_field);
				if (propertyValue != null)
					lucene_doc.add(_Keyword(s_field, propertyValue));
			}
		// Set extends values
		if (doc.GetExtendValues() != null) {
			for (String key : doc.GetExtendValues().keySet()) {
				String value = doc.GetExtendValues().get(key);
				lucene_doc.add(_Keyword(key, value));
			}
		}
		// Set indexed field
		String[] indexFields = doc.GetIndexFields();
		for (String idx_field : indexFields) {
			String propertyValue = _GetField(doc, idx_field);
			if (StringUtils.isNotBlank(propertyValue))
				lucene_doc.add(_Index(idx_field, propertyValue));
		}

		// Set extends values
		if (doc.GetExtendIndexValues() != null) {
			for (String key : doc.GetExtendIndexValues().keySet()) {
				String value = doc.GetExtendIndexValues().get(key);
				try {
					lucene_doc.add(_Index(key, value));
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		return lucene_doc;
	}
	
	@SuppressWarnings("unused")
	private static Object _DocumentToObject(Class<? extends SearchEnabled> clazz, Document doc)
			throws Exception {
		Object obj = clazz.newInstance();
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		for(java.lang.reflect.Field f : fields){
			String fieldName = f.getName();
			String fieldValue = doc.get(fieldName);
			if(fieldName == null || fieldName.equals("") || fieldValue == null || fieldValue.equals(""))
				continue;
			System.out.println(fieldName+"-----"+fieldValue);
			//设置值
			Reflections.invokeSetter(obj, fieldName, fieldValue);
		}
		return obj;
	}

	/**
	 * 访问对象某个属性的值
	 * @param obj
	 * @param field
	 * @return
	 */
	private static String _GetField(Object obj, String field) throws Exception {
		Object fieldValue = PropertyUtils.getProperty(obj, field);
		if (fieldValue instanceof String)
			return (String) fieldValue;
		if (fieldValue instanceof Date)
			return DateFormatUtils.format((Date) fieldValue, _FMT_DATE);
		return String.valueOf(fieldValue);
	}
	

	/**
	 * 关键字——
	 * @param name
	 * @param value
	 * @return
	 */
	private static final Field _Keyword(String name, String value) {
		return new Field(name, value, Field.Store.YES, Field.Index.ANALYZED);
	}

	/**
	 * 索引字段——分词
	 * @param name
	 * @param value
	 * @return
	 */
	private static final Field _Index(String name, String value) {
		return new Field(name, value, Field.Store.YES, Field.Index.ANALYZED);
	}

}
