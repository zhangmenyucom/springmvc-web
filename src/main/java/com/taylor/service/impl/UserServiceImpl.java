package com.taylor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.taylor.dao.UserDao;
import com.taylor.entity.User;
import com.taylor.service.UserService;

@Service
@Qualifier("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public User loadUserByUsername(String username) {
		User user = new User();
		user.setId(1);
		return userDao.selectOne(user);
	}
}
