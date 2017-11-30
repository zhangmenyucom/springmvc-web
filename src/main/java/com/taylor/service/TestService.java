package com.taylor.service;

import com.taylor.common.CrudService;
import com.taylor.entity.Demo;
import com.taylor.entity.TestEntity;

public interface TestService  extends CrudService<TestEntity,TestEntity>{

  Demo test();
}
