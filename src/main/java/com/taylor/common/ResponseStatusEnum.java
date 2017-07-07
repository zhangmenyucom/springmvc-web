package com.taylor.common;

public enum ResponseStatusEnum implements  ResponseCode{
	
	SUCCESS(0,"success"),
	FAILED(1,"failed"),
	UNKNOWNED_ERROR(2,"未知错误"),
	ARGS_ERROR(3,"参数错误"),
	CAPTHCA_NOT_CORRECT(3000, "capthca not correct"),
	UNHANDLE_EXCEPTION(8080001, "Unhandle Exception"),
	/*******subAccount*************/
	
	SUBACCOUNT_TEL_EXITS(10001,"当前手机号已被使用！"),


	/**********Auth**************/
	SESSION_EXPIRED(80101004,"登录信息已失效"),
	AUTHENTICATION_EXCEPTION(80101005, "登录信息异常"),
	SESSION_NOT_MATCH(80101010,"登录信息不匹配"),
	LEGAL_NOT_SIGN(80101012,"未签署电子协议"),
	UTH_PASSWORD_NEED_RESET(80101013,"请重置密码"),
	MIMI_PROGRAM_NOT_AUTHORIZED(80101005,"小程序未绑定"),

	/************日志*************/
	WRITE_LOG_TO_BUFFER_FAILED(901001001,"日志存入Buffer失败")
	;
	private long code;
	private String message;
	
	private ResponseStatusEnum(long code, String value){
		this.code = code;
		this.message = value ;
	}

	public long getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
