package com.taylor.security;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
public class GrantedAuthorityImpl implements GrantedAuthority {

	private static final long serialVersionUID = 3621779722647409327L;

	private String authName;

	@Override
	public String getAuthority() {
		return this.authName;
	}

	public GrantedAuthorityImpl(String authName) {
		this.authName = authName;
	}

}
