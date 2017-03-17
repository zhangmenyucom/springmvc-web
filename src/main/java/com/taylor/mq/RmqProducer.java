package com.taylor.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RmqProducer {

	@Autowired
	private AmqpTemplate amqpTemplate;

	/**
	 * 发送信息
	 * @param msg
	 */
	public void sendMessage(RmqMessage msg) {
		try {
			// 发送信息
			amqpTemplate.convertAndSend(msg.getExchange(), msg.getRouteKey(), msg);
			System.out.println("发送消息成功");
		} catch (Exception e) {
			System.out.println("发送消息失败");
			e.printStackTrace();
		}
	}
}