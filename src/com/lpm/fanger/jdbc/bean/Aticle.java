package com.lpm.fanger.jdbc.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.dbcp.ConnectionFactory;
import org.springframework.stereotype.Component;

import com.lpm.fanger.jdbc.mysql.BaseDaoMysqlImpl;
import com.lpm.fanger.search.base.SearchEnabled;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-22
 */
@Table(name="t_article")
public class Aticle implements SearchEnabled{
	private Long id;
	private String title;
	private String tag;
	private String content; 
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	@Override
	public String[] GetStoreFields() {
		return new String[]{"tag"};
	}
	@Override
	public String[] GetIndexFields() {
		return new String[]{"title","content"};
	}
	@Override
	public HashMap<String, String> GetExtendValues() {
		return null;
	}
	@Override
	public HashMap<String, String> GetExtendIndexValues() {
		return null;
	}
	
	@Override
	public List<? extends SearchEnabled> ListAfter(long id, int count) {
		return null;
	}
	
	@Override
	public float GetBoost() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String toString() {
		return "Aticle [id=" + id + ", title=" + title + ", tag=" + tag
				+ ", content=" + content + "]";
	}
	
}
