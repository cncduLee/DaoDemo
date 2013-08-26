package com.lpm.fanger.jdbc.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.*;
/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-23
 */
public class AticleRowMapper implements RowMapper<Aticle>{

	@Override
	public Aticle mapRow(ResultSet rs, int value) throws SQLException {
		Aticle al = new Aticle();
		al.setId(Long.parseLong(rs.getString("id")));
		al.setContent(rs.getString("content"));
		al.setTag(rs.getString("tag"));
		al.setTitle(rs.getString("title"));
		return al;
	}

}
