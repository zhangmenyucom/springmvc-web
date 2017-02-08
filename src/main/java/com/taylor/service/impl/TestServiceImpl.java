package com.taylor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taylor.dao.TestDao;
import com.taylor.entity.Test;
import com.taylor.service.TestService;

@Service
public class TestServiceImpl implements TestService{
	@Autowired
	private TestDao testDao;
	
	@Override
	@Transactional
	public List<Test> queryTest(Test test){
		return testDao.select(test);
	}

}
