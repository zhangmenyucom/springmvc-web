package com.taylor.common;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService<Entity, Query, Dao extends BaseDao<Entity, Query>> {
    
    @Autowired
    private Dao dao;

    public Dao getDao() {
        return dao;
    }
    public void setDao(Dao dao) {
        this.dao = dao;
    }
}
