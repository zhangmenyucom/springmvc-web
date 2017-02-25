package com.taylor.common;

public interface ErrorCode {

    public static final int SUCCESS = 0;
    public static final int FAILED = 40001;
    public static final int ORDER_DISEXIST = 40002;
    public static final int PAYSTATUS_ERROR = 40003;

    public String getMsg();

    int getCode();

    Integer getKey();

    String getDescription();

}
