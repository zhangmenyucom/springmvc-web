package com.taylor.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.taylor.dao.TestDao;
import com.taylor.entity.TestEntity;

public class TestDaoTest extends BaseDaoTest {

	@Autowired
	private TestDao testDao;

	@Test
	public void test() {
		TestEntity entity = new TestEntity();
		entity.setId(2);
		System.out.println(testDao.get(entity).getName());
	}

}
