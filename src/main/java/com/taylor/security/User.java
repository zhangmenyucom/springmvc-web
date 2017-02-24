package com.taylor.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
	private String name;
	private String password;
	private List<GrantedAuthority> list;
}
