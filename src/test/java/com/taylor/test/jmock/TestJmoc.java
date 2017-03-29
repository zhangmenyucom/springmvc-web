package com.taylor.test.jmock;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.taylor.service.UserService;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class TestJmoc {
    @Mocked // 用@Mocked标注的对象，不需要赋值，jmockit自动mock
    private MyObject obj;

    @Mocked
    private UserService userService;

    @Test
    public void testHello() {
        new NonStrictExpectations() {// 录制预期模拟行为
            {
                obj.hello("Zhangsan");
                result = "Hello Zhangsan";

                obj.hello("Zhangsan");
                result = "Hello lisi";

                obj.hello("wangwu");
                result = "Hello wangwu";
            }
        };
        assertEquals("Hello lisi", obj.hello("Zhangsan"));// 调用测试方法
        assertEquals("Hello lisi", obj.hello("Zhangsan"));// 调用测试方法
        assertEquals("Hello wangwu", obj.Hi("wangwu"));// 调用测试方法
        
        
        


/*        new Verifications() {// 验证预期Mock行为被调用 以及调用次数
            {
                this.invoke(obj, "hello", "Zhangsan");
                this.invoke(obj, "hello", "Zhangsan");
                this.invoke(obj, "hello", "wangwu");
            }
        };*/
    }

    @Test
    public void testGetMessage() {
        new NonStrictExpectations() {
            {
                userService.getMessage();
                result = "hello world!";
            }
        };
        assertEquals("hello world!", userService.getMessage());
        assertEquals("hello world!", userService.getMessage());

        new Verifications() {
            {
                this.invoke(userService, "getMessage");
                times =2;
            }
        };
    }

    @Test
    public void testMutiResult() {
        new NonStrictExpectations() {
            {
                userService.getName("zhangsan");
                result = "zhangsan";
                // userService.getName("lisi");
                result = "lisi";
                // userService.getName("wangwu");
                result = "wangwu";
            }
        };
        assertEquals("zhangsan", userService.getName("zhangsan"));
        assertEquals("lisi", userService.getName("zhangsan"));
        assertEquals("wangwu", userService.getName("zhangsan"));

        new Verifications() {
            {
                this.invoke(userService, "getName", "zhangsan");
                times = 3;
                /*
                 * this.invoke(userService, "getName","lisi"); times = 1; this.invoke(userService,
                 * "getName","wangwu"); times = 1;
                 */
            }
        };
    }
}

