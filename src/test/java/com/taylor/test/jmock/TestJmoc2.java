package com.taylor.test.jmock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.taylor.service.TestService;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring-application.xml"})
@TransactionConfiguration
@Transactional
public class TestJmoc2 {

    @Autowired
    @Injectable
    private TestService testService;

    @Tested
    private MyObject myObject;
    
    @Mocked
    private MyObject myObject2;

    @Test
    public void testGetTestMsg() {
        new MockUp<MyObject>() {
            @Mock
            public String getTestMsg(Integer id) {
                return testService.getByPrimaryKey(id).getName();
            }
        };
    	
    	
        System.out.println(new MyObject().getTestMsg(1));
    }
    
    @Test
    public void testGetTestMsg2() {
    	
    	new Expectations() {
    		{
    			myObject2.getTestMsg((Integer)any);
    			result="haha";
    		}
		};
    			 

    	System.out.println(myObject2.getTestMsg(1));
    }
    
    @Test
    public void testInjectable(){
    	myObject.getTestMsg(1);
    	
    }
}

