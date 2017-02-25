package com.taylor.test.jmock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.taylor.entity.TestEntity;
import com.taylor.service.TestService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring-application.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional
public class TestUnit {
    @Autowired
    private TestService testService;

    @Test
    public void testGet() {
        TestEntity test = new TestEntity();
        test.setId(1);
        Assert.assertSame(true, testService.get(test) != null);
    }

    @Test
    public void TestSave() {
        TestEntity test = new TestEntity();
        test.setName("雷蕾1");
        testService.save(test);
        Assert.assertSame(true, test.getId()!=null);
    }

    @Test
    public void TestQueryList() {
        TestEntity query = new TestEntity();
        query.setName("雷蕾");
        Assert.assertSame(3, testService.find(query).size());
    }
}
