package com.taylor.common;

import java.util.List;

public interface CrudService<Entity,Query> {
	
	  public Entity save(Entity entity);

	  public Entity update(Entity entity);

	  public Entity updateByPrimaryKeySelective(Entity entity);

	  public void del(Entity entity);

	  public void delByPrimaryKey(Object id);
	  
	  public Entity get(Entity entity);

	  public Entity getByPrimaryKey(Object id);

	  public List<Entity> find(Query query);

	  public int findTotalCount(Query query);
	  
	  public boolean exist(Entity entity);


}
