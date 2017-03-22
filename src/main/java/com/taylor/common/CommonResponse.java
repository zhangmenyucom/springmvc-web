package com.taylor.common;

import lombok.AllArgsConstructor;

@lombok.Data
@AllArgsConstructor
public class CommonResponse<T> {
	private int status;
	private String message;
	private T Data;
}
