package com.lpm.fanger.search.base;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

/**
 * @Intro lucene索引工具类
 * @author Lee
 * @Date 2013-8-22
 */
public class IndexUtils {
	
	private final static String Globle_Lucene_Path = "D:/lucene_index";
	private final static String KeyWord_Field_Name= "id";
	private final static IKAnalyzer Globle_Analyzer = new IKAnalyzer();
	private final static String FMT_DATE = "yyyyMMddHHmmssSSS";
	private final static NumberFormat FMT_ID = NumberFormat.getInstance();
	
	static {
		FMT_ID.setGroupingUsed(false);
		FMT_ID.setMaximumFractionDigits(0);
		FMT_ID.setMaximumIntegerDigits(12);
		FMT_ID.setMinimumIntegerDigits(12);
	}
	
	private IndexUtils(){}
	/**
	 * 当前分词器
	 * @return
	 */
	public final static Analyzer getAnalyzer(){
		return Globle_Analyzer;
	}
	
	/*********************CRUD************************/
	/*********************CRUD************************/
	/*********************CRUD************************/
	
	/**
	 * 添加索引（建立索引）
	 * @param clazz 目标对象
	 * @param docs 目标对象详细集合
	 * @return 成功添加索引的条数
	 * @throws Exception 
	 */
	public static int add(
			Class<? extends LuceneEnable> clazz,
			List<? extends LuceneEnable> objs) throws Exception{
		if (objs == null || objs.size() == 0)
			return 0;
		IndexWriter writer = getWriter(clazz);
		try {
			int count = add(writer,objs);
			writer.optimize();
			return count;
		}finally{
			writer.close();
			writer = null;
		}
	}
	
	/**
	 * 添加速印（建立索引）
	 * @param doc 当前文档
	 * @throws Exception 
	 */
	public static void add(LuceneEnable doc) throws Exception{
		if(doc == null)
			return;
		IndexWriter writer = getWriter(doc.getClass());
		try{
			//再添加
			writer.addDocument(objectToDocment(doc));
			//提交事务
			writer.commit();
		}finally{
			writer.close();
		}
	}
	/**
	 * 删除索引
	 * @param doc
	 * @throws Exception 
	 */
	public static void delete(LuceneEnable doc) throws Exception{
		if(doc == null)
			return;
		IndexWriter writer = getWriter(doc.getClass());
		try{
			writer.deleteDocuments(new Term("id", String.valueOf(doc.getPrimeryKey())));
			writer.commit();
		}finally{
			writer.close();
		}
	}
	/**
	 * 更新索引
	 * @param doc
	 * @throws Exception 
	 */
	public static void update(LuceneEnable doc) throws Exception{
		if(doc == null)
			return;
		IndexWriter writer = getWriter(doc.getClass());
		try{
			//先删除
			writer.deleteDocuments(new Term("id", String.valueOf(doc.getPrimeryKey())));
			//再添加
			writer.addDocument(objectToDocment(doc));
			//提交事务
			writer.commit();
		}finally{
			writer.close();
		}
	}
	/**********查找**********/
	/**********查找**********/
	
	/**
	 * 索引库中查找满足条件的主键结果集
	 * @param clazz
	 * @param query
	 * @param maxCount
	 * @return 满足条件的主键结果集
	 * @throws Exception
	 */
	public static List<Long> find(
			Class<? extends LuceneEnable> clazz,
			Query query,int maxCount) throws Exception{
		IndexSearcher reader = getReader(clazz);
		try{
			//获取查询结果
			TopDocs hits = reader.search(query, null,maxCount);
			if(hits == null)
				return null;
			List<Long> results = new ArrayList<Long>();
			//取得结果数
			int num = Math.min(hits.totalHits, maxCount);
			for(int i = 0; i < num ;i++){
				ScoreDoc scoreDoc = hits.scoreDocs[i];
				Document doc = reader.doc(scoreDoc.doc);
				Long primaryKey = NumberUtils.toLong(doc.get(KeyWord_Field_Name));
				if(primaryKey > 0 && !results.contains(primaryKey)){
					//满足条件值，加到结果集合
					results.add(primaryKey);
				}
			}
			return results;
		}finally{
			reader.close();
		}
	}
	
	/**
	 * 索引库中查找满足条件的【对象】结果集
	 * @param clazz
	 * @param query
	 * @param maxCount
	 * @return
	 * @throws Exception
	 */
	public static List<? extends LuceneEnable> findList(
			Class<? extends LuceneEnable> clazz,
			Query query,int maxCount) throws Exception{
		IndexSearcher reader = getReader(clazz);
		List results = new ArrayList();
		try{
			TopDocs hits = reader.search(query, null, maxCount);
			if(hits == null){
				return null;
			}
			//找最小集合长度
			int num = Math.min(hits.totalHits, maxCount);
			for(int i=0;i<num;i++){
				//循环找到对象集合
				ScoreDoc scoreDoc = hits.scoreDocs[i];
				Document doc = reader.doc(scoreDoc.doc);
				//实例化对象属性
				Object obj = documentToObject(clazz, doc);
				if(obj != null){
					results.add(obj);
				}
			}
			return results;
		}finally{
			reader.close();
		}
	}

	/**
	 * 获取全文查询对象
	 * 
	 * 任意参数
	 * @param booleanClauses
	 * @return
	 */
	public static BooleanQuery getFullTextQuery(BooleanClause... booleanClauses){
		BooleanQuery booleanQuery = new BooleanQuery();
		for (BooleanClause booleanClause : booleanClauses){
			booleanQuery.add(booleanClause);
		}
		return booleanQuery;
	}

	/**
	 * 获取全文查询对象
	 * @param q 查询关键字
	 * @param fields 查询字段（任意多）
	 * @return 全文查询对象
	 */
	public static BooleanQuery getFullTextQuery(String q, String... fields){
		Analyzer analyzer = new IKAnalyzer();
		BooleanQuery query = new BooleanQuery();
		try {
			if (q != null && !q.equals("")){
				for (String field : fields){
					QueryParser parser = new QueryParser(Version.LUCENE_36, field, analyzer);   
					query.add(parser.parse(q), Occur.SHOULD);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return query;
	}
		
	
	/************助手方法**************/
	/************助手方法**************/
	/************助手方法**************/
	
	/**
	 * 添加索引助手类
	 * @param indexWriter
	 * @param docs
	 * @return
	 */
	protected static int add(IndexWriter writer,List<? extends LuceneEnable> objs) throws Exception{
		if(objs == null || objs.size() == 0){
			return 0;
		}
		int count = 0;
		for(LuceneEnable obj : objs){
			Document doc = objectToDocment(obj);
			doc.setBoost(obj.GetBoost());
			writer.addDocument(doc);
			count++;
		}
		return count;
	}
	/**
	 * 获取索引写
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	protected static IndexWriter getWriter(Class<?> clazz) throws IOException{
		String path = Globle_Lucene_Path + File.separator + clazz.getSimpleName();
		Directory indexDir = FSDirectory.open(new File(path));
		return new IndexWriter(
				indexDir, 
				Globle_Analyzer,
				IndexWriter.MaxFieldLength.UNLIMITED);
	}
	/**
	 * 获取索引读
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	protected static IndexSearcher getReader(Class<?> clazz) throws IOException{
		String path = Globle_Lucene_Path + File.separator + clazz.getSimpleName();
		Directory indexDir = FSDirectory.open(new File(path));
		IndexSearcher reader = new IndexSearcher(indexDir);
		//使用ik的相似度评估器
		Similarity similarity = new IKSimilarity();
		reader.setSimilarity(similarity);
		return reader;
	}
	/**
	 * Document转换成对象
	 * @param clazz
	 * @param doc
	 * @return
	 * @throws Exception 
	 */
	private static Object documentToObject(Class<? extends LuceneEnable> clazz,Document doc) throws Exception{
		Object obj = clazz.newInstance();
		java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
		for(java.lang.reflect.Field field : fields){
			String name = field.getName();
			String value = doc.get(name);
			if(name ==null || name.equals("") || value ==null || value.equals("") )
				continue;//进入一个字段
			setFieldValue(obj, name, value);//需要调试
		}
		return null;
	}
	
	/**
	 * 对象转换成Documents
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
	private static Document objectToDocment(LuceneEnable obj) throws Exception{
		Document doc = new Document();
		//设置关键字域
		doc.add(keyWord(KeyWord_Field_Name, FMT_ID.format(obj.getPrimeryKey())));
		//设置索引域
		String[] indexFields = obj.GetIndexFields();
		if(indexFields != null && indexFields.length > 0){
			for(String indexField : indexFields){
				String value = getFieldValue(obj, indexField);
				if(value != null && !value.equals("")){
					doc.add(index(indexField, value));
				}
			}
		}
		//设置存储域
		String[] storeFields = obj.GetStoreFields();
		if(storeFields != null && storeFields.length > 0){
			for(String storeField : storeFields){
				String value = getFieldValue(obj, storeField);
				if(value != null && !value.equals("")){
					doc.add(keyWord(storeField, value));
				}
			}
		}
		//设置扩展索引值
		HashMap<String, String> extendIndex = obj.GetExtendIndexValues();
		if(extendIndex != null){
			for(String key : extendIndex.keySet()){
				String value = extendIndex.get(key);
				doc.add(index(key, value));
			}
		}
		//设置扩展值
		HashMap<String, String> extend = obj.GetExtendValues();
		if(extend != null){
			for(String key : extend.keySet()){
				String value = extend.get(key);
				doc.add(keyWord(key, value));
			}
		}
		return doc;
	}
	
	/**
	 * 构造关键字域
	 * @param name
	 * @param value
	 * @return （关键字）域/字段
	 */
	private static final Field keyWord(String name,String value){
		return new Field(name, value, Field.Store.YES, Field.Index.NOT_ANALYZED);
	}
	/**
	 * 构造索引域
	 * @param name
	 * @param value
	 * @return （索引）域/字段
	 */
	private static final Field index(String name,String value){
		return new Field(name, value, Field.Store.YES, Field.Index.ANALYZED);
	}
	
	/**
	 * 获取对象属性值
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws Exception 只支持属性类型为String/integer/double/float等基本类型
	 */
	private static String getFieldValue(Object obj,String fieldName) throws Exception{
		Object fieldValue = PropertyUtils.getProperty(obj, fieldName);
		if(fieldValue instanceof String)
			return (String)fieldValue;
		if(fieldValue instanceof Date)
			return DateFormatUtils.format((Date)fieldValue, FMT_DATE);
		return String.valueOf(fieldValue);
	}
	
	/**
	 * 设置属性值
	 * @param obj
	 * @param fieldName
	 * @param fieldValue
	 * @throws Exception 只支持属性类型为String/integer/double/float等基本类型
	 */
	private static void setFieldValue(Object obj,String fieldName,String fieldValue) throws Exception{
		PropertyUtils.setProperty(obj, fieldName, fieldValue);
	}
}
