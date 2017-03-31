package com.taylor.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.taylor.entity.TestEntity;

public class TestServiceTest extends BaseServiceTest {

	@Autowired
	private TestService testService;

	@Test
	public void testTestService() {
		TestEntity entity = new TestEntity();
		entity.setId(2);
		assertEquals("helloworld~", testService.find(entity).get(0).getName());
	}

}
