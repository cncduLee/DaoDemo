package com.lpm.fanger.search.base;

import java.util.HashMap;
import java.util.List;

/**
 * @Intro 支持搜索lucene全文检索
 * 功能的Bean类需要实现该接口
 * @author Lee
 * @Date 2013-8-24
 */
public interface LuceneEnable {
	/**
	 * 获取搜索对象的关键字,
	 * 便于搜索得到分析后，得到记录的主键值，
	 * 这样就可以通过查数据库表的方式，来得
	 * 到记录的完整情况
	 * @return
	 */
	public Long getPrimeryKey();
	
	/**
	 * 返回搜索对象需要存储的字段名，例如createTime, author等
	 * @return
	 */
	public String[] GetStoreFields();

	/**
	 * 返回搜索对象的索引字段，例如title,content
	 * @return
	 */
	public String[] GetIndexFields();
	
	/**
	 * 返回对象的扩展信息
	 * @return
	 */
	public HashMap<String, String> GetExtendValues();

	/**
	 * 返回对象的扩展索引信息
	 * @return
	 */
	public HashMap<String, String> GetExtendIndexValues();
	
	/**
	 * 返回文档的权重
	 * @return
	 */
	public float GetBoost();

}
