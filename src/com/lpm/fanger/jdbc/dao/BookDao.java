package com.lpm.fanger.jdbc.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lpm.fanger.jdbc.bean.Aticle;
import com.lpm.fanger.jdbc.bean.AticleRowMapper;
import com.lpm.fanger.jdbc.bean.Book;
import com.lpm.fanger.jdbc.bean.BookRowMapper;
import com.lpm.fanger.jdbc.mysql.BaseDaoMysqlImpl;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-23
 */
@Component("bookDao")
public class BookDao extends BaseDaoMysqlImpl<Book, Long>{
	public BookDao(){
		super(Book.class);
	}
	
	public List<Book> listAfter(Long begain,Integer count){
		List<Object> values = new ArrayList<Object>();
		values.add(begain);
		values.add(count);
		String sql = "select * from "+getTableName()+" limit ?,?";
		List<Book> list = search(sql, values,new BookRowMapper());
		return list;
	}
}

