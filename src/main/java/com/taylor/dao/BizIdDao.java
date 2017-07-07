package com.taylor.dao;

/**
 * @author Michael.Wang
 * @date 2017/4/28
 */

import com.taylor.common.BaseDao;
import com.taylor.entity.TestEntity;
import com.taylor.uuid.entity.BizIdEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DAO for biz_id
 */
public interface BizIdDao extends BaseDao<BizIdEntity, BizIdEntity> {
    BizIdEntity queryByBizType(String bizType);

    List<BizIdEntity> queryByModule(String module);

    List<BizIdEntity> queryAll();

    void initAll(String module);
}

