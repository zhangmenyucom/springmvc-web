package com.taylor.test.jmock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.taylor.mq.RmqMessage;
import com.taylor.mq.RmqProducer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring-application.xml" })
public class TestMq {
	@Resource
	private RmqProducer rmqProducer;

	@Test
	public void test() throws IOException {

		String exchange = "testExchange";//// 交换器
		String routeKey = "testQueue";// 队列
		// 参数
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("data", "hello");

		RmqMessage msg = new RmqMessage(exchange, routeKey, param);
		// 发送消息
		rmqProducer.sendMessage(msg);

	}
}
