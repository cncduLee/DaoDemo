package com.lpm.fanger.search.base;

import java.util.List;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-22
 */
public interface FullTextSearch<T> {
	public List<T> getAll(String queryStr);
	boolean createIndex();
}
