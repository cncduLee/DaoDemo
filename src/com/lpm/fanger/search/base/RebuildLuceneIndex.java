package com.lpm.fanger.search.base;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.queryParser.QueryParserTokenManager;
import org.apache.lucene.search.Query;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.wltea.analyzer.lucene.IKQueryParser;

import com.lpm.fanger.jdbc.bean.Aticle;
import com.lpm.fanger.jdbc.bean.Book;
import com.lpm.fanger.jdbc.dao.AticleDao;
import com.lpm.fanger.jdbc.dao.BookDao;
import com.lpm.fanger.jdbc.dao.ExampleDao;

/**
 * @Intro 定时更新索引库
 * @author Lee
 * @Date 2013-8-22
 */
public class RebuildLuceneIndex {
	private final static Log log = LogFactory.getLog(RebuildLuceneIndex.class);
	private final static int BATCH_COUNT = 500;
	
//	static BookDao dao;
//	static AticleDao dao;
	static ExampleDao dao;
	
	static{
		ApplicationContext app = new ClassPathXmlApplicationContext("spring.xml");
//		dao = app.getBean("bookDao", BookDao.class);
//		dao = app.getBean("aticleDao", AticleDao.class);
		dao = app.getBean("exampleDao", ExampleDao.class);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {

		String beanName = Example.class.getName();//Book.class.getName();//Aticle.class.getName();//
		Class beanClass = Class.forName(beanName);
//		Long t1 = System.currentTimeMillis();
//		int ic = _BuildIndexOfObject(beanClass);
//		log.info(ic + " documents of " + beanName + " created.");
//		System.out.println("TIME:"+(System.currentTimeMillis() - t1)+"ms");

		Long t2 = System.currentTimeMillis();
		Query query =// LuceneSearchUtils.getFullTextQuery("神奇校车", new String[]{"bookName"});//,"outline"}
		IKQueryParser.parseMultiField(new String[]{"title"}, "选择");//经过测试，这个方法比较好一点
		//LuceneSearchUtils.getFullTextQuery("java", new String[]{"book_name","out_line"});//IKQueryParser.parseMultiField(new String[]{"title","content"}, "c++");
		
		List<Long> list = IndexUtils.find(beanClass, query, 100);//LuceneIndexUtils.find(beanClass, query, 100);
		//List<Aticle> list = (List<Aticle>) LuceneIndexUtils.find(beanClass, query, 100, false);
		//List<Book> list = (List<Book>) LuceneIndexUtils.find(beanClass, query, 100, false);
		System.out.println(list.size());
		System.out.println("TIME:"+(System.currentTimeMillis() - t2)+"ms");
		System.exit(0);
	}
	
	/**
	 * 构建索引
	 * @param objClass
	 * @return
	 */
	private static int _BuildIndexOfObject(Class<? extends LuceneEnable> objClass) throws Exception {
		int ic = 0;
		long last_id = 0L;
		do {
			List<? extends LuceneEnable> objs = dao.listAfter(last_id,BATCH_COUNT);
			if(objs != null && objs.size()>0){
				ic  += IndexUtils.add(objClass, objs);
				last_id = objs.get(objs.size()-1).getPrimeryKey();
			}
			if(objs == null || objs.size() < BATCH_COUNT)
				break;
		}while(true);
		
		return ic;
	}
}
