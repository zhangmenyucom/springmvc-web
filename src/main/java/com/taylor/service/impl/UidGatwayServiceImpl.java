package com.taylor.service.impl;

import com.taylor.service.BizIdService;
import com.taylor.service.UidGateWayService;
import com.taylor.uuid.client.impl.BufferUpdateThread;
import com.taylor.uuid.client.impl.NumHolder;
import com.taylor.uuid.entity.BizIdEntity;
import com.taylor.uuid.loader.UidConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * taylor.zhang
 */
@Slf4j
@Service
public class UidGatwayServiceImpl implements UidGateWayService {

    private static Boolean isInited = Boolean.FALSE;

    @Autowired
    private BizIdService bizIdService;

    /**
     * 获取uid
     *
     * @param bizTypeWithPostfix 业务类型,如store_order_id, store_order_no
     * @return uid
     */
    @Override
    public long generateUID(String bizTypeWithPostfix) {
        long uid = NumHolder.getNextId(bizTypeWithPostfix);
        log.debug("maxId  generated is :{}",uid);
        return uid;
    }

    /**
     * 使用前务必调用此方法进行初始化，如果在容器中使用，需要在Listener中进行初始化
     */
    @Override
    public synchronized void init() {
        if (!isInited) {
            //从本地加载全部记录
            UidConfig.init("http://localhost:8080/", "", 1);
            List<BizIdEntity> entities = bizIdService.queryAll();
            NumHolder.initSlots(entities);
            Thread thread = BufferUpdateThread.getThread();
            thread.setDaemon(true);
            thread.start();
        }
    }
}
