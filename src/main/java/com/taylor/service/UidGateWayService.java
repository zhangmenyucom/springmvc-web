package com.taylor.service;


public interface UidGateWayService {
    void init();
    long generateUID(String bizType);
}
