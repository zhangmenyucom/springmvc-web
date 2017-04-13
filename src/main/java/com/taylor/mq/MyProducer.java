package com.taylor.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;


public class MyProducer extends DefaultMQProducer {

	private final Logger logger = LoggerFactory.getLogger(MyProducer.class);

	public void init() throws MQClientException {
		// 参数信息
		logger.info("MyProducer initialize!");
		logger.info(this.getProducerGroup());
		logger.info(this.getNamesrvAddr());
		logger.info(this.getInstanceName());
		this.setVipChannelEnabled(false);
		this.start();
	}

	public void destroy() {
		this.shutdown();
	}
}