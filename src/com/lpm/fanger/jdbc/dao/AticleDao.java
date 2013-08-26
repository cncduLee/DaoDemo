package com.lpm.fanger.jdbc.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.lpm.fanger.jdbc.bean.Aticle;
import com.lpm.fanger.jdbc.bean.AticleRowMapper;
import com.lpm.fanger.jdbc.mysql.BaseDaoMysqlImpl;
import com.lpm.fanger.search.base.SearchEnabled;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-22
 */
@Repository("aticleDao")
public class AticleDao extends BaseDaoMysqlImpl<Aticle, Long>{
	public AticleDao() {
		super(Aticle.class);
	}
	public List<Aticle> listAfter(Long begain,Integer count){
		List<Object> values = new ArrayList<Object>();
		values.add(begain);
		values.add(count);
		String sql = "select * from "+getTableName()+" limit ?,?";
		List<Aticle> list = search(sql, values,new AticleRowMapper());
		return list;
	}
}
