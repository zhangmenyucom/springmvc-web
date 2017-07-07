package com.taylor.common;

import java.util.List;

public interface CrudService<Entity,Query> {
	
	  Entity save(Entity entity);

	  Entity update(Entity entity);

	  Entity updateByPrimaryKeySelective(Entity entity);

	  void del(Entity entity);

	  void delByPrimaryKey(Object id);
	  
	  Entity get(Entity entity);

	  Entity getByPrimaryKey(Object id);

	  List<Entity> find(Query query);

	  int findTotalCount(Query query);
	  
	  boolean exist(Entity entity);


}
