package com.taylor.uuid.client.impl;


import com.taylor.uuid.client.UidGenerator;

import java.util.Random;

/**
 * 随机数id生成器
 */
public class RandomIdGenerator implements UidGenerator {

    @Override
    public long getUID(String bizType) {
        long r = new Random().nextLong();
        r = r < 0 ? -r : r;
        return r;
    }

    @Override
    public long[] getUIDs(String bizType, int number) {
        if (number <= 0) {
            return new long[0];
        }
        long[] ids = new long[number];
        for (int i = 0; i < number; i++) {
            ids[i] = getUID(bizType);
        }
        return ids;
    }
}
