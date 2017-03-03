package com.taylor.common;

public enum MqTypeEnum {

	TOPIC(1, "发布订阅"), QUEUE(2, "消息队列");

	private Integer key;
	
	private String description;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	MqTypeEnum(Integer key, String description) {
		this.key = key;
		this.description = description;
	}

}
