package com.taylor.test.jmock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.taylor.mq.RabbitMessage;
import com.taylor.mq.RmqProducer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring-application.xml" })
@TransactionConfiguration
public class TestMq {
	@Resource
	private RmqProducer rmqProducer;

	@Test
	public void test() throws IOException {

		String exchange = "testExchange";//// 交换器
		String routeKey = "testQueue";// 队列
		String methodName = "test";// 调用的方法
		// 参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("data", "hello");

		RabbitMessage msg = new RabbitMessage(exchange, routeKey, methodName, param);
		// 发送消息
		rmqProducer.sendMessage(msg);

	}

}
