package com.lpm.fanger.search.base;

import java.util.HashMap;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-24
 */
@Table(name="t_article")
public class Example implements LuceneEnable{
	
	private Integer id;
	private String title;
	private String content;
	private String tag;
	
	/************getter and setter**************/
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	

	
	/************override method**************/
	@Override
	public Long getPrimeryKey() {
		return Long.valueOf(this.getId());
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
	public float GetBoost() {
		return 0;
	}

}
