package com.lpm.fanger.jdbc.mysql;

import org.springframework.stereotype.Repository;

import com.lpm.fanger.jdbc.dao.ModelDao;
import com.lpm.fanger.jdbc.model.OneModel;

@Repository("modelDao")
public class ModelDaoImpl extends BaseDaoMysqlImpl<OneModel, Integer> implements ModelDao {
	public ModelDaoImpl() {
		super(OneModel.class);
	}
}
