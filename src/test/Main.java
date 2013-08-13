package test;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.demo.dao.ModelDao;
import com.demo.model.OneModel;

public class Main {
	private ModelDao modelDao;
	public void setModelDao(ModelDao modelDao) {
		this.modelDao = modelDao;
	}

	public static void main(String[] args) {
		new Main().demo();
	}
	
	public void demo(){
		ApplicationContext context=new ClassPathXmlApplicationContext("spring.xml");
		modelDao=context.getBean("modelDao",ModelDao.class);
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
}
