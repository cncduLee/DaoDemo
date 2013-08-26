package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lpm.fanger.jdbc.bean.Aticle;
import com.lpm.fanger.jdbc.dao.AticleDao;
import com.lpm.fanger.jdbc.dao.ModelDao;
import com.lpm.fanger.jdbc.model.OneModel;

public class Main {
//	private ModelDao modelDao;
//	public void setModelDao(ModelDao modelDao) {
//		this.modelDao = modelDao;
//	}
	

	public static void main(String[] args) {
		new Main().index();
	}
	
	
	
	
	public void index(){
		ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
		AticleDao aticleDao = context.getBean("aticleDao",AticleDao.class);
		
		long ct = System.currentTimeMillis();
//		for(int a = 0;a<1000;a++){
//			List<Aticle> list = new ArrayList<Aticle>();
//			for(int b=0;b<10;b++){
//				Aticle aticle = new Aticle();
//				aticle.setContent(a + "content 选择可以体现您的情绪或个性的主题模板！还可以按您自己的想法设置首页排版方式。选择可以体现您的情绪或个性的主题模板！还可以按您自己的想法设置首页排版方式。"+a);
//				aticle.setTitle("选择可以体现"+a);
//				aticle.setTag("jjj");
//				list.add(aticle);
//			}
//			aticleDao.batchSave(list);
//		}
//		butch(aticleDao);
	}
	
	public static void butch(AticleDao aticleDao){
		long ct = System.currentTimeMillis();
		//创建一个固定大小[10]的线程池
		ExecutorService pool = Executors.newFixedThreadPool(10);
		for(int a=0;a<10;a++){
			pool.execute(new Butch(aticleDao));
		}
		pool.shutdown();
		if(pool.isShutdown()){
			System.out.printf("\nTIME %d ms", (System.currentTimeMillis() - ct) );
			System.out.println(aticleDao.getCount("", null));
		}
	}
	
	
	/**
	public void demo(){
		ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
		modelDao=context.getBean("modelDao",ModelDao.class);
		
		modelDao=context.getBean("aticle",ModelDao.class);
		
		//增
		OneModel model=new OneModel();
		model.setDate(new Date());
		model.setName("随便写");
		model=modelDao.save(model);
		Integer id=model.getId();
		//查一下
		model=modelDao.get(id);
		System.out.println(model);
		//改
		model.setName("随便写");
		modelDao.update(model);
		//再查一下
		model=modelDao.get(id);
		System.out.println(model);
		//删了吧
		modelDao.del(id);
	}
	**/
}

class Butch implements Runnable{
	
	AticleDao aticleDao;
	
	public Butch(AticleDao aticleDao){
		this.aticleDao = aticleDao;
	}
	
	@Override
	public void run() {
		List<Aticle> list = new ArrayList<Aticle>();
		for(int b=0;b<100;b++){
			Aticle aticle = new Aticle();
			aticle.setContent(b + "content 选择可以体现您的情绪或个性的主题模板！还可以按您自己的想法设置首页排版方式。选择可以体现您的情绪或个性的主题模板！还可以按您自己的想法设置首页排版方式。");
			aticle.setTitle("选择可以体现"+b);
			aticle.setTag("jjj");
			list.add(aticle);
		}
		aticleDao.batchSave(list);
	}
}