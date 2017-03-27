package com.taylor.mq;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

import lombok.Data;

//单个消息监听接口
@Data
public class RocketMessageListener implements MessageListenerConcurrently {
	// 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		MessageExt msg = msgs.get(0);
		System.out.println("我消费了一笔" + msg.getBody().toString());
		// 如果没有return success ，consumer会重新消费该消息，直到return success
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}