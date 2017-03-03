package com.taylor.common;

import java.util.Collections;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractCrudService<Entity, Query, Dao extends BaseDao<Entity, Query>> extends BaseService<Entity, Query, Dao> implements CrudService<Entity, Query> {


    @Override
    @Transactional
    public Entity save(Entity entity) {
        if (entity == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(),RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        this.getDao().save(entity);
        return entity;
    }
    
    @Override
    @Transactional
    public Entity update(Entity entity) {
        if (entity == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        this.getDao().update(entity);
        return entity;
    }

    @Override
    public Entity updateByPrimaryKeySelective(Entity entity) {
        if (entity == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        this.getDao().updateByPrimaryKeySelective(entity);
        return entity;
    }

    @Override
    @Transactional
    public void del(Entity entity) {
        if (entity == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        this.getDao().del(entity);
    }

    @Override
    @Transactional
    public void delByPrimaryKey(Object id) {
        if (id == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), RETURN_CODE.ARGS_EMPTY.getMsg());
        }
        this.getDao().delByPrimaryKey(id);
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
    public int findTotalCount(Query query) {
        if (query == null) {
            throw new ManagerException(RETURN_CODE.ARGS_EMPTY.getCode(), "鏌ヨ鏉′欢瀵硅薄涓虹┖");
        }
        Integer total = this.getDao().findTotalCount(query);
        return total == null ? 0 : total;
    }
}
