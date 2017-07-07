package com.taylor.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 为了同时兼容目前返回数据的两种方式（ModelAndView&CommonResponse）
 * ,同时为了更好地与springmvc容器集成，将CommonResponse改为继承ModelAndView
 *
 * @param <T> 自定义的对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -7921893893735709488L;

	private long code;
	private String message;
	private String traceId;
	T data = null;

	public CommonResponse(ResponseCode responseCode, T data) {
		this(responseCode.getCode(), responseCode.getMessage(), null, data);
	}

	public CommonResponse(ResponseCode responseCode) {
		this(responseCode.getCode(), responseCode.getMessage(), null, null);
	}

	public CommonResponse(long code, String message) {
		this(code, message, null, null);
	}


	public CommonResponse(int code, String message) {
		this(code, message, null, null);
	}


	/*
	直接对象构造，默认成功。
	 */
	public CommonResponse(T data){
		this(ResponseStatusEnum.SUCCESS.getCode(),ResponseStatusEnum.SUCCESS.getMessage(),null, data);
	}

}
