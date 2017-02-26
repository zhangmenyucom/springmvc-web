package com.taylor.test.jmock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.taylor.service.TestService;
import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring-application.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional
public class TestJmoc2 {

    @Autowired
    private TestService testService;

    @Tested
    private MyObject myObject;

    @Test
    public void testGetTestMsg() {
        new MockUp<MyObject>() {
            @Mock
            public String getTestMsg(Integer id) {
                return testService.getByPrimaryKey(id).getName();
            }
        };
        System.out.println(myObject.getTestMsg(2));
    }
}

