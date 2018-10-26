package com.taylor.common;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;


/**
 * @author Taylor
 */
public abstract class BaseServiceImpl<Entity, Query, Dao extends BaseDao<Entity, Query>> implements BaseService<Entity, Query> {

    @Autowired
    private Dao dao;

    public Dao getDao() {
        return this.dao;
    }

    @Override
    public Entity save(Entity entity) {
        if (entity == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        this.getDao().save(entity);
        return entity;
    }

    @Override
    public int update(Entity entity) {
        if (entity == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        return this.getDao().update(entity);
    }

    @Override
    public int updateByPrimaryKeySelective(Entity entity) {
        if (entity == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        return this.getDao().updateByPrimaryKeySelective(entity);
    }

    @Override
    public int del(Entity entity) {
        if (entity == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        return this.getDao().del(entity);
    }

    @Override
    public int delByPrimaryKey(Object id) {
        if (id == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        return this.getDao().delByPrimaryKey(id);
    }

    @Override
    public boolean exist(Entity entity) {
        return this.get(entity) != null;
    }

    @Override
    public Entity get(Entity entity) {
        if (entity == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        return this.getDao().get(entity);
    }

    @Override
    public Entity getByPrimaryKey(Object id) {
        return this.getDao().getByPrimaryKey(id);
    }

    @Override
    public List<Entity> find(Query query) {
        if (query == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        List<Entity> list = this.getDao().findByCondition(query);
        if (list == null) {
            list = Collections.emptyList();
        }
        return list;
    }

    @Override
    public Integer findTotalCount(Query query) {
        if (query == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), "参数错误");
        }
        Integer total = this.getDao().findTotalCount(query);
        return total == null ? 0 : total;
    }

    @Override
    public List<Entity> findByCondition(Query query) {
        return this.getDao().findByCondition(query);
    }


}
