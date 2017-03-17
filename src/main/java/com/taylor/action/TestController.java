package com.taylor.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taylor.entity.TestEntity;
import com.taylor.mq.RmqMessage;
import com.taylor.mq.RmqProducer;
import com.taylor.service.TestService;

@RequestMapping("/test")
@Controller
public class TestController extends BaseAction {

	@Autowired
	private TestService testService;

	@Autowired
	private RmqProducer producer;

	@ResponseBody
	@RequestMapping("/query")
	public List<TestEntity> queryTest(TestEntity test, HttpServletRequest request, HttpServletResponse response) {
		log.debug("这只是一个测试");
		return testService.find(test);
	}

	/**
	 * @Description: 消息队列
	 * @Author:
	 * @CreateTime:
	 */
	@ResponseBody
	@RequestMapping("/sendQueue")
	public String testQueue() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", "hello rabbitmq");
			RmqMessage msg = new RmqMessage("testQueueExchange", "test.Queue", map);
			producer.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "发送完毕";
	}

	/**
	 * @Description: 消息topic队列
	 * @Author:
	 * @CreateTime:
	 */
	@ResponseBody
	@RequestMapping("/sendTopic")
	public String testTopic() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", "hello rabbitmq");
			RmqMessage msg = new RmqMessage("testTopicExchange", "testQueque", map);
			producer.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "发送完毕";
	}

	/**
	 * @Description: 消息fanout队列
	 * @Author:
	 * @CreateTime:
	 */
	@ResponseBody
	@RequestMapping("/sendFanout")
	public String testFanout() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", "hello rabbitmq");
			RmqMessage msg = new RmqMessage("testFanoutExchange", null, map);
			producer.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "发送完毕";
	}
}
