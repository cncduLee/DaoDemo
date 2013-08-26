package com.lpm.fanger.jdbc.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.*;
/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-23
 */
public class BookRowMapper implements RowMapper<Book>{

	@Override
	public Book mapRow(ResultSet rs, int value) throws SQLException {
		Book al = new Book();
		al.setId(Long.parseLong(rs.getString("id")));
		al.setBookName(rs.getString("book_name"));
		al.setOutline(rs.getString("outline"));
		al.setPress(rs.getString("press"));
		al.setAuthor(rs.getString("author"));
		return al;
	}

}
