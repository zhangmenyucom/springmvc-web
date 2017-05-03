package com.taylor.common;

import com.taylor.data.game.UserInfoAndLatestResult;

import lombok.Data;

@Data
public class CommonResponse {
	private Integer error;// ": 0,
	private String code;// "null,
	private String message;// "请求成功",
	private UserInfoAndLatestResult data;//
}
