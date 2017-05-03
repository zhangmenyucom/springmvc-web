package com.taylor.yuanbo.order;

import com.taylor.common.JsonUtil;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Singapore30Order {
	private String lottery;
	private String issue;
	private String method;
	private String content;
	private String model;
	private Integer multiple;
	private Integer code;
	private boolean compress;

	public static void main(String[] args) {
		System.out.println(JsonUtil.transfer2JsonString(new Singapore30Order("t1s30","","dxdsh", "单双,单", "jiao", 1, 1950, false)));
		System.out.println("["+JsonUtil.transfer2JsonString(new Singapore30Order("t1s30","","dxdsh", "单双,单", "jiao", 1, 1950, false))+"]");
	}
}
