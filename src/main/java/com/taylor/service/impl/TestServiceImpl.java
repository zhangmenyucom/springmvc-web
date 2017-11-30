package com.taylor.service.impl;

import com.taylor.annotation.TestAnnotation;
import com.taylor.common.AbstractCrudService;
import com.taylor.dao.TestDao;
import com.taylor.entity.Demo;
import com.taylor.entity.TestEntity;
import com.taylor.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @author taylor
 */
@Service
public class TestServiceImpl extends AbstractCrudService<TestEntity, TestEntity, TestDao> implements TestService {

    @Override
    @TestAnnotation(name = "hahah", type = 2)
    public Demo test() {
        System.out.println("hahah");
        Demo demo = new Demo();
        demo.setName("zhangxiaolu");
        return demo;
    }


}
