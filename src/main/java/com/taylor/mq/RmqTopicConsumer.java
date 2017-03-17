package com.taylor.mq;

import java.util.Map;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.taylor.common.JsonMessageConverter;

public class RmqTopicConsumer implements MessageListener {
	private static int count = 0;

	public void onMessage(Message message) {
		RmqMessage rmqMessage = JsonMessageConverter.getObjectFromMessage(message);
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) rmqMessage.getContent();
		System.out.println("Topic 消息消费者 消费" + ++count + "次,content:" + map.get("data"));
	}

}
