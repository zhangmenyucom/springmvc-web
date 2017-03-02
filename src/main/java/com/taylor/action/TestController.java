package com.taylor.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taylor.amq.producer.SendMessage;
import com.taylor.entity.TestEntity;
import com.taylor.service.TestService;

@RequestMapping("/test")
@Controller
public class TestController extends BaseAction {
	@Autowired
	@Qualifier("sendQueueMessage")
	private SendMessage sendQueueMessage;

	@Autowired
	@Qualifier("sendTopicMessage")
	private SendMessage sendTopicMessage;

	@Autowired
	private TestService testService;

	@ResponseBody
	@RequestMapping("/query")
	public List<TestEntity> queryTest(TestEntity test, HttpServletRequest request, HttpServletResponse response) {
		log.debug("这只是一个测试");
		return testService.find(test);
	}

	/**
	 * @desc sendQueue(发送消息队列)
	 * @param message
	 * @author xiaolu.zhang
	 * @date 2017年3月2日 下午3:30:15
	 */
	@RequestMapping(value = "/queue/{message}")
	@ResponseBody
	public String sendQueue(@PathVariable("message") String message) {
		sendQueueMessage.sendMessage(message);
		return "success";
	}

	/**
	 * @desc sendTopic(发送主题订阅)
	 * @param message
	 * @author xiaolu.zhang
	 * @date 2017年3月2日 下午3:30:37
	 */
	@RequestMapping(value = "/topic/{message}")
	@ResponseBody
	public String sendTopic(@PathVariable("message") String message) {
		sendTopicMessage.sendMessage(message);
		return "success";
	}
}
