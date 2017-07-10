package com.taylor.common;

public interface ErrorCode {

    int SUCCESS = 0;
    int FAILED = 40001;
    int ORDER_DISEXIST = 40002;
    int PAYSTATUS_ERROR = 40003;

    String getMsg();

    int getCode();

    Integer getKey();

    String getDescription();

}
