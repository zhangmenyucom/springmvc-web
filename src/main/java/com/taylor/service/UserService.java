package com.taylor.service;

import com.taylor.entity.User;

public interface UserService {
	public User loadUserByUsername(String username);
}
