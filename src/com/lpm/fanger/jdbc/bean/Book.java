/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-7-30
 */
package com.lpm.fanger.jdbc.bean;

import java.util.HashMap;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lpm.fanger.search.base.SearchEnabled;

/**
 * 书籍基本信息表
 * @author Lee
 */
@Table(name = "TB_BOOK")
public class Book implements SearchEnabled{
	
	private Long id;//主键
	
	private String bookName;//图书名
	private String author;//作者
	private String isbn;//图书编号
	private String translator;//译者
	private String press;//出版社
	private String version;//版本
	private String outline;//图书概述
	private String delFlag;	//删除标记（0：正常；1：删除）
	private String coverPic;//封面图片
	
	//直接属性，并无关联
	private String catLog;//目录：格式为：[{"第1章",1},{"第2章",23},{"第3章",55}]Set<Map<String,Integer>>
	
	private boolean isFree;//免费阅读：true,收费false
	private Double price;//价格
	
	private Integer wantRead;//想读人数
	private Integer likeCount;//喜欢人数
	private Integer isReading;//在读人数
	private Integer hasRead;//读过人数
	private Integer commentCount;//读过人数

	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTranslator() {
		return translator;
	}
	public void setTranslator(String translator) {
		this.translator = translator;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getOutline() {
		return outline;
	}
	public void setOutline(String outline) {
		this.outline = outline;
	}
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getWantRead() {
		return wantRead;
	}
	public void setWantRead(Integer wantRead) {
		this.wantRead = wantRead;
	}
	public Integer getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}
	public Integer getIsReading() {
		return isReading;
	}
	public void setIsReading(Integer isReading) {
		this.isReading = isReading;
	}
	public Integer getHasRead() {
		return hasRead;
	}
	public String getCoverPic() {
		return coverPic;
	}
	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}
	public void setHasRead(Integer hasRead) {
		this.hasRead = hasRead;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Book() {
	}
	public Book(String bookName, String author, String isbn) {
		this.bookName = bookName;
		this.author = author;
		this.isbn = isbn;
	}
	
	public String getCatLog() {
		return catLog;
	}
	public void setCatLog(String catLog) {
		this.catLog = catLog;
	}
	
	@Override
	public String[] GetStoreFields() {
//		return new String[]{"author","press"};
		return null;
	}
	@Override
	public String[] GetIndexFields() {
		return new String[]{"bookName","outline"};
	}
	@Override
	public HashMap<String, String> GetExtendValues() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public HashMap<String, String> GetExtendIndexValues() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<? extends SearchEnabled> ListAfter(long id, int count) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public float GetBoost() {
		// TODO Auto-generated method stub
		return 0;
	}
		
}
