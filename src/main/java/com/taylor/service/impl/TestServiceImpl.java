package com.taylor.service.impl;

import com.taylor.common.BaseServiceImpl;
import org.springframework.stereotype.Service;
import com.taylor.dao.TestDao;
import com.taylor.entity.TestEntity;
import com.taylor.service.TestService;

/**
 * @author Taylor
 */
@Service
public class TestServiceImpl extends BaseServiceImpl<TestEntity, TestEntity, TestDao> implements TestService{

}
