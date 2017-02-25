package com.taylor.test.jmock;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class TestJmoc {
    @Mocked // 用@Mocked标注的对象，不需要赋值，jmockit自动mock
    MyObject obj;

    @Test
    public void testHello() {
        new NonStrictExpectations() {// 录制预期模拟行为
            {
                obj.hello("Zhangsan");
                //returns("Hello Zhangsan");
                // 也可以使用：result = "Hello Zhangsan";
                result = "Hello Zhangsan";
            }
        };
        for (int i = 0; i < 3; i++) {
            assertEquals("Hello Zhangsan", obj.hello("Zhangsan"));// 调用测试方法
        }
        new Verifications() {// 验证预期Mock行为被调用 以及调用次数
            {
                this.invoke(obj, "hello", "Zhangsan");
                times = 3;
            }
        };
    }
}
