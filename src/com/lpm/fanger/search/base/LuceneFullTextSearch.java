package com.lpm.fanger.search.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.lpm.fanger.jdbc.bean.Aticle;
import com.lpm.fanger.jdbc.dao.AticleDao;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-22
 */
public class LuceneFullTextSearch<T> implements FullTextSearch<T> {
	private final String INDEXPATH = "g:\\index";
	IKAnalyzer analyzer = new IKAnalyzer();
	private final static int BATCH_COUNT = 500;

	
	@Autowired
	AticleDao aticleDao;
	
	@Override
	public List<T> getAll(String queryStr) {
		
		return null;
	}

	@Override
	public boolean createIndex() {
		int ic = 0;
		int last_id = 0;
		
		if(isIndexExit())
			return true;
		do{	
			List<Aticle> objs = aticleDao.listAfter(last_id, BATCH_COUNT);
			if(objs != null && objs.size()>0){
				ic  += LuceneIndexUtils.add(objClass, objs);
				last_id = objs.get(objs.size()-1).getId();
			}
			if(objs == null || objs.size() < BATCH_COUNT)
				break;
		}while(true);
		
		return false;
	}
	
	private boolean isIndexExit(){
		File f = new File(INDEXPATH);
		if(f.listFiles().length > 0)
			return true;
		return false;
	}
	
	
	
}
