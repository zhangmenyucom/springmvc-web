package com.taylor.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MyConsumer extends DefaultMQPushConsumer {

	private final Logger logger = LoggerFactory.getLogger(MyConsumer.class);

	private RocketMessageListener concurrentlyMessageListener;

	private String topic;

	private String tag;

	/**
	 * Spring bean init-method
	 */
	public void init() throws InterruptedException, MQClientException {
		// 参数信息
		logger.info("DefaultMQPushConsumer initialize!");
		logger.info(this.getConsumerGroup());
		logger.info(this.getNamesrvAddr());
		logger.info(this.getInstanceName());

		// 订阅指定MyTopic下tags等于MyTag
		this.subscribe(topic, tag);
		this.registerMessageListener(this.concurrentlyMessageListener);
		// Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
		this.start();
		logger.info("MyConsumer start success!");
	}
	
	/**
	 * Spring bean destroy-method
	 */
	public void destroy() {
		this.shutdown();
	}

}