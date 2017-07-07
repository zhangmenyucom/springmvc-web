package com.taylor.uuid.client;

/**
 * @author Michael.Wang
 * @date 2017/4/23
 */
public interface UidGenerator {
    /**
     * 获取uid
     * @param bizTypeWithPostfix 带后缀的业务类型，如store_order_id, store_order_no等
     *
     * @return UID
     */
    long getUID(String bizTypeWithPostfix);

    /**
     * 批量获取uid
     * @param bizTypeWithPostfix 带后缀的业务类型，如store_order_id, store_order_no等
     *
     * @return UID
     */
    long[] getUIDs(String bizTypeWithPostfix, int number);

}
