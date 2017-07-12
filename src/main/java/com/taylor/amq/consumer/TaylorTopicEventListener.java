package com.taylor.amq.consumer;

import com.taylor.common.MqTypeEnum;
import com.taylor.entity.AmqEntity;
import com.taylor.service.AmqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@Component
@Qualifier("taylorTopicEventListener")
public class TaylorTopicEventListener implements MessageListener {
	@Autowired
	private AmqService amqService;

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				TextMessage msg = (TextMessage) message;
				amqService.save(new AmqEntity(MqTypeEnum.TOPIC.getKey(), msg.getText()));
				System.out.println("收到主题消息："+msg.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
