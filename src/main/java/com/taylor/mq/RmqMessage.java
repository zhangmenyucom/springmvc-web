package com.taylor.mq;

import java.io.Serializable;

import lombok.Data;

@Data
public class RmqMessage implements Serializable {
	private static final long serialVersionUID = -6487839157908352120L;

	private String exchange;// 交换器

	private Object content; // 消息内容

	private String routeKey;// 路由key

	public RmqMessage(){
		
	}

	public RmqMessage(String exchange, String routeKey, Object content) {
		this.exchange = exchange;
		this.routeKey = routeKey;
		this.content = content;
	}
}