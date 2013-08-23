package com.lpm.fanger.search.base;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-22
 */
public interface IndexUtils {
	
	/**
	 * 添加文档
	 * @param doc
	 * @throws Exception
	 */
	void add(SearchEnabled doc) throws Exception;
	/**
	 * 删除文档
	 * @param doc
	 * @throws Exception
	 */
	void delete(SearchEnabled doc) throws Exception;
	/**
	 * 添加文档
	 * @param clazz
	 * @param docs
	 * @return
	 */
	int add(Class<? extends SearchEnabled> clazz,List<? extends SearchEnabled> docs);
	/**
	 * 添加文档
	 * @param indexWriter
	 * @param docs
	 * @return
	 */
	int add(IndexWriter indexWriter,List<? extends SearchEnabled> docs);
	/**
	 * 从索引库中检索
	 * @param beanClass
	 * @param query
	 * @param max_count
	 * @return
	 * @throws IOException
	 */
	List<Long> find(Class<? extends SearchEnabled> beanClass, Query query, int max_count) throws IOException;
	
	/**
	 * 获取索引写
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	IndexWriter getWrite(Class<?> clazz) throws IOException;
	
	/**
	 * 获取索引读
	 * @param clazz
	 * @return
	 * @throws IOException
	 */
	IndexSearcher getSearch(Class<?> clazz) throws IOException;
	
	
}
