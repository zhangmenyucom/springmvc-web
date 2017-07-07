package com.taylor.service;


import com.taylor.uuid.entity.BizIdEntity;

import java.util.List;

/**
 * @author Michael.Wang
 * @date 2017/4/28
 */
public interface BizIdService {

    /**
     * 获取下一号段最大id
     *
     * @param bizType
     * @return
     */
    long fetchNextMaxId(String bizType);

    /**
     * 获取全部记录
     *
     * @return
     */
    List<BizIdEntity> fetchAll(String module);

    /**
     * 获取全部记录
     *
     * @return
     */
    List<BizIdEntity> queryAll();


}
