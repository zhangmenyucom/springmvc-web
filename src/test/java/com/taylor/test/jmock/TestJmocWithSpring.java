package com.taylor.test.jmock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.taylor.entity.TestEntity;
import com.taylor.service.TestService;
import com.taylor.service.UserService;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring-application.xml"})
@TransactionConfiguration
public class TestJmocWithSpring {

    @Autowired
    private TestService testService;

    @Mocked
    private MyObject myObject;

    @Mocked
    private UserService userService;


    @Test
    public void testGet() {
        new NonStrictExpectations(myObject) {// 录制预期模拟行为
            {
                myObject.hello(anyString);
                result = "hello zhansan";
            }
        };
        System.out.println(myObject.Hi("lilei"));
        System.out.println(testService.save(new TestEntity("haha")) + myObject.hello("zhansan"));
    }

    @Test
    public void testExpectations() {
        new Expectations() {// 录制预期模拟行为
            {
                userService.getMessage();
                result = "hello world!";
                minTimes = 2;
                userService.getName("test");
                result = "hello world2!";
                minTimes = 2;
            }
        };

        System.out.println(testService.save(new TestEntity("haha")).getName() + "  " + userService.getMessage());
        System.out.println(testService.save(new TestEntity("haha")).getName() + "  " + userService.getMessage());
        System.out.println(testService.save(new TestEntity("haha")).getName() + "  " + userService.getName("test"));
        System.out.println(testService.save(new TestEntity("haha")).getName() + "  " + userService.getName("test"));
    }


    @Test
    public void testNonStrictExpectations() {
        new NonStrictExpectations() {// 录制预期模拟行为
            {
                userService.getMessage();
                result = "hello world!";
                minTimes = 2;
                userService.getName("test");
                result = "hello world2!";
                minTimes = 2;
            }
        };

        System.out.println(testService.save(new TestEntity("haha")).getName() + "  " + userService.getMessage());
        System.out.println(testService.save(new TestEntity("haha")).getName() + "  " + userService.getName("test"));
        System.out.println(testService.save(new TestEntity("haha")).getName() + "  " + userService.getMessage());
        System.out.println(testService.save(new TestEntity("haha")).getName() + "  " + userService.getName("test"));


        new Verifications() {
            {
                this.invoke(userService, "getMessage");
                times = 2;
                this.invoke(userService, "getName", "test");
                times = 2;
            }
        };
    }


}

