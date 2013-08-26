package com.lpm.fanger.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.lpm.fanger.jdbc.mysql.BaseDaoMysqlImpl;
import com.lpm.fanger.search.base.Example;

/**
 * @Intro db interface
 * @author Lee
 * @Date 2013-8-26
 */
@Repository("exampleDao")
public class ExampleDao extends BaseDaoMysqlImpl<Example, Integer>{
	public ExampleDao(){
		super(Example.class);
	}
	
	public List<Example> listAfter(Long begain,Integer count){
		List<Object> values = new ArrayList<Object>();
		values.add(begain);
		values.add(count);
		String sql = "select * from "+getTableName()+" limit ?,?";
		List<Example> list = search(sql, values,new ExampleRowMappere());
		return list;
	}
}

class ExampleRowMappere implements RowMapper<Example>{

	@Override
	public Example mapRow(ResultSet rs, int value) throws SQLException {
		Example ex = new Example();
		ex.setContent(rs.getString("content"));
		ex.setTitle(rs.getString("title"));
		ex.setTag(rs.getString("tag"));
		ex.setId(rs.getInt("id"));
		return ex;
	}
	
}
