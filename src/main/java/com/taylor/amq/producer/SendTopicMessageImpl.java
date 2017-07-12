package com.taylor.amq.producer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
@Qualifier("sendTopicMessage")
public class SendTopicMessageImpl implements SendMessage {

	@Autowired
	@Qualifier("jmsTopicTemplate")
	private JmsTemplate jmsTemplate;

	@Autowired
	@Qualifier("taylorTopic")
	private Topic topic;

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	public void sendMessage(final String message) {
		jmsTemplate.send(topic, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
        System.out.println("发送主题消息成功");
    }
}