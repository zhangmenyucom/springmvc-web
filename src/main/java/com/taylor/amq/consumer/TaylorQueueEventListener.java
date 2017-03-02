package com.taylor.amq.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("taylorQueueEventListener")
public class TaylorQueueEventListener implements MessageListener {
	@Override
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			try {
				TextMessage msg = (TextMessage) message;
				System.out.println(Thread.currentThread().getName()+"---"+msg.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}

		}
	}
}
