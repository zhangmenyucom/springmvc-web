package com.taylor.common;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

/**
 * DAO类,其它DAO类都要继承该类
 * 
 * @param <Entity>
 */
public interface BaseDao<Entity, Query> extends Mapper<Entity> {

    public Entity get(@Param(value = "entity") Entity entity);

    public Entity getByPrimaryKey(@Param(value = "id") Object id);

    public List<Entity> findByCondition(@Param(value = "query") Query query);

    public Integer findTotalCount(@Param(value = "query") Query query);

    public int save(@Param(value = "entity") Entity entity);

    public int update(@Param(value = "entity") Entity entity);

    public int del(@Param(value = "entity") Entity entity);

    public int delByPrimaryKey(@Param(value = "id") Object id);

    public int updateByPrimaryKeySelective(@Param(value = "entity") Entity entity);
}
