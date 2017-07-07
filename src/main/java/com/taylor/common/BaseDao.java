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

    Entity get(@Param("entity") Entity entity);

    Entity getByPrimaryKey(@Param("id") Object id);

    List<Entity> findByCondition(@Param("query") Query query);

    Integer findTotalCount(@Param("query") Query query);

    int save(@Param("entity") Entity entity);

    int update(@Param("entity") Entity entity);

    int del(@Param("entity") Entity entity);

    int delByPrimaryKey(@Param("id") Object id);

    @Override
    int updateByPrimaryKeySelective(@Param("entity") Entity entity);
}
