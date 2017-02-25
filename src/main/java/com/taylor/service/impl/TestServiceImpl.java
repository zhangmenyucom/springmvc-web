package com.taylor.service.impl;

import org.springframework.stereotype.Service;

import com.taylor.common.AbstractCrudService;
import com.taylor.dao.TestDao;
import com.taylor.entity.TestEntity;
import com.taylor.service.TestService;

@Service
public class TestServiceImpl extends  AbstractCrudService<TestEntity, TestEntity, TestDao> implements TestService{



  

}
