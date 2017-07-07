package com.taylor.uuid.client.impl;

import com.taylor.uuid.client.UidGenerator;
import com.taylor.uuid.loader.UidLoader;
import lombok.extern.slf4j.Slf4j;

/**
 * 单据类型，可用最大号，当前使用号
 *
 * @author Michael.Wang
 * @date 2017/4/24
 */
@Slf4j
public class BufferedUidGenerator implements UidGenerator {

    @Override
    public long getUID(String bizTypeWithPostfix) {
        try {
            return NumHolder.getNextId(bizTypeWithPostfix);
        } catch (Exception e) {
            log.warn("get uid from local slots failed. try to get uid from remote slots", e);
            return UidLoader.fetchDirect(bizTypeWithPostfix);
        }
    }

    @Override
    public long[] getUIDs(String bizTypeWithPostfix, int number) {
        if (number <= 0) {
            return new long[0];
        }
        long[] ids = new long[number];
        for (int i = 0; i < number; i++) {
            try {
                ids[i] = getUID(bizTypeWithPostfix);
            } catch (Exception e) {
                log.error("fail to generate id", e);
            }
        }
        return ids;
    }
}
