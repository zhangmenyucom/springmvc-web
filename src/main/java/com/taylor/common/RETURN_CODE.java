package com.taylor.common;

public enum RETURN_CODE {

	// common
	SUCCESS								(0, "success"),
	FAIL								(7999999, "fail"),
	SUBMIT_SUCCESS(1,"操作成功"),
	SUBMIT_FAIL(7999998,"操作失败"),
	//StoreTerminal
	STORETERMINAL_NOT_EXITS (100001,"storeTerminal not exits"),
	STORETERMINAL_NOT_AVAILABLE(100002,"设备已占用，请重新选择！"),
	STORETERMINAL_REMOVE_SUCCESS(100003,"删除成功"),
	STORETERMINAL_REMOVE_FAIL(100004,"删除失败"),
	REFLECT_METHOD_INVOKE_ERROR(100005, "反射方法调用失败"),
	// merchantInfo
	MERCHANT_INFO_NOT_COMPLETE(11001, "商户信息不完整"), ARGS_EMPTY(11002,"参数为空"),
	;
	class ErrorEntity {
		public int errcode;
		public String errmsg;
	}

	public ErrorEntity entity = new ErrorEntity();
	
	private RETURN_CODE(int errcode, String errmsg) {
		this.entity.errcode = errcode;
		this.entity.errmsg = errmsg;
	}
	
	public int getCode() {
		return this.entity.errcode;
	}
	
	public String getMsg() {
		return this.entity.errmsg;
	}
}
