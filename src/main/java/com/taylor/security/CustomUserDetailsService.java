package com.taylor.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.extern.log4j.Log4j;

@Log4j
public class CustomUserDetailsService implements UserDetailsService {

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {

		UserDetails user = null;
		try {
			System.out.println(username + "   用户页面输入的用户名");
			user = new User("zhangsan", "123", getAuthorities(0));
		} catch (Exception e) {
			throw new UsernameNotFoundException("异常处理：检索用户信息未通过！");
		}
		return user;
	}

	/**
	 * 获得访问角色权限列表
	 * 
	 * @param access
	 * @return
	 */
	public Collection<GrantedAuthority> getAuthorities(Integer role) {
		System.out.println("取得的权限是  :" + role);
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

		// 所有的用户默认拥有ROLE_USER权限
		if (role == 0) {
			System.out.println("普通用户");
			log.debug("取得普通用户权限-->");
			authList.add(new GrantedAuthorityImpl("ROLE_USERS"));
		}
		// 如果参数role为1.则拥有ROLE_ADMIN权限
		if (role == 1) {
			log.debug("取得ADMIN用户权限-->");
			authList.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
		}
		System.out.println(authList.size() + "  权限列表长度");
		return authList;
	}
}