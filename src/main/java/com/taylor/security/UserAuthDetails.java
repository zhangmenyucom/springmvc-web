package com.taylor.security;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.security.core.GrantedAuthority;

/**
 * 用户权限详细类型
 * @author Miao
 *
 */
@AllArgsConstructor
@EqualsAndHashCode
@Data
public class UserAuthDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String merchantId;
	private Long userId;
	private boolean isSuperAdmin;
	
	private Collection<? extends GrantedAuthority> authorities;

	/**
	 * 授权门店
	 */
	private Collection<? extends GrantedAuthority> authorityStores;
	
	public UserAuthDetails(){
		
	}

}
