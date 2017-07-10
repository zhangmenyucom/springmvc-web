package com.taylor.service;


public interface UidGateWayService {
    void init(String module);
    long generateUID(String bizType);
}
