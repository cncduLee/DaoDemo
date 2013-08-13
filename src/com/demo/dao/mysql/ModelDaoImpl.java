package com.demo.dao.mysql;

import org.springframework.stereotype.Repository;

import com.demo.dao.ModelDao;
import com.demo.model.OneModel;
@Repository("modelDao")
public class ModelDaoImpl extends BaseDaoMysqlImpl<OneModel, Integer> implements ModelDao {
	public ModelDaoImpl() {
		super(OneModel.class);
	}
}
