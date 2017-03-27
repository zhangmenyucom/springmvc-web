package com.taylor.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSONObject;
import com.taylor.common.CommonResponse;
import com.taylor.entity.TestEntity;
import com.taylor.mq.MyProducer;
import com.taylor.service.TestService;

@RequestMapping("/test")
@Controller
public class TestController extends BaseAction {

	@Autowired
	private TestService testService;
	@Autowired
	private MyProducer myProducer;

	@ResponseBody
	@RequestMapping("/query")
	public List<TestEntity> queryTest(TestEntity test, HttpServletRequest request, HttpServletResponse response) {
		log.debug("这只是一个测试");
		return testService.find(test);
	}

	@RequestMapping("/sendMessage")
	@ResponseBody
	public CommonResponse<String> sendMessage() throws Exception {
		Message msg = new Message("MyTopic", "MyTag", (JSONObject.toJSONString("123")).getBytes());
		SendResult sendResult = null;
		sendResult = myProducer.send(msg);
		// 当消息发送失败时如何处理
		if (sendResult == null || sendResult.getSendStatus() != SendStatus.SEND_OK) {
			// TODO
		}
		return new CommonResponse<String>(0, "成功", "发射成功");
	}
}
