package com.taylor.common.utils;

import java.util.UUID;

/**
 * Created by ZhaoChao on 2016/3/5.
 */
public final class UuidUtil {
    private UuidUtil() {
    }

    /**
     * generate serial no by uuid
     * @return serial no
     */
    public static String getSerialNo() {
        UUID uuid = UUID.randomUUID();
        String serialNo = uuid.toString().replaceAll("\\-", "");
        return serialNo;
    }
}
