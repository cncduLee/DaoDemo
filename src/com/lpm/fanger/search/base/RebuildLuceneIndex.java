package com.lpm.fanger.search.base;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lpm.fanger.jdbc.bean.Aticle;
import com.lpm.fanger.jdbc.dao.AticleDao;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-22
 */
public class RebuildLuceneIndex {
	private final static Log log = LogFactory.getLog(RebuildLuceneIndex.class);
	private final static int BATCH_COUNT = 500;
	
	static AticleDao dao;
	
	static{
		ApplicationContext app = new ClassPathXmlApplicationContext("spring.xml");
		dao = app.getBean("aticleDao", AticleDao.class); 
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {
		Long t1 = System.currentTimeMillis();
		String beanName = Aticle.class.getName();
		Class beanClass = Class.forName(beanName);
		int ic = _BuildIndexOfObject(beanClass);
		log.info(ic + " documents of " + beanName + " created.");
		System.out.println("TIME:"+(System.currentTimeMillis() - t1)+"ms");
		System.exit(0);
	}
	
	/**
	 * 构建索引
	 * @param objClass
	 * @return
	 */
	private static int _BuildIndexOfObject(Class<? extends SearchEnabled> objClass) throws Exception {
		int ic = 0;
		long last_id = 0L;
		do {
			List<? extends SearchEnabled> objs = dao.listAfter(last_id, BATCH_COUNT);
			if(objs != null && objs.size()>0){
				ic  += LuceneIndexUtils.add(objClass, objs);
				last_id = objs.get(objs.size()-1).getId();
			}
			if(objs == null || objs.size() < BATCH_COUNT)
				break;
		}while(true);
		
		return ic;
	}
}
