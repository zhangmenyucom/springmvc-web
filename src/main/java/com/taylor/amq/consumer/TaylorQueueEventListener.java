package com.taylor.amq.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.taylor.common.MqTypeEnum;
import com.taylor.entity.AmqEntity;
import com.taylor.service.AmqService;

@Component
@Qualifier("taylorQueueEventListener")
public class TaylorQueueEventListener implements MessageListener {
	@Autowired
	private AmqService amqService;

	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				TextMessage msg = (TextMessage) message;
				amqService.save(new AmqEntity(MqTypeEnum.QUEUE.getKey(),Thread.currentThread().getName() + "---" + msg.getText()));
				System.out.println(Thread.currentThread().getName() + "---" + msg.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}

		}
	}
}
