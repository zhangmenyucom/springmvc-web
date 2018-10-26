package com.taylor.common;

import java.util.List;

/**
 * @author Taylor
 */
public interface BaseService<Entity, Query> {

    boolean exist(Entity entity);

    Entity get(Entity entity);

    Entity getByPrimaryKey(Object id);

    List<Entity> findByCondition(Query query);

    List<Entity> find(Query query);

    Integer findTotalCount(Query query);

    Entity save(Entity entity);

    int update(Entity entity);

    int updateByPrimaryKeySelective(Entity entity);

    int del(Entity entity);

    int delByPrimaryKey(Object id);
}
