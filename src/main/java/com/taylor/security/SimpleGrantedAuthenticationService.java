package com.taylor.security;

import java.util.Collection;

/**
 * 简单授权认证实现
 * 需要与request请求在同一种线程中使用，不支持在新开启的线程中使用
 * @author miao
 *
 */
public class SimpleGrantedAuthenticationService implements GrantedAuthenticationService{

	@Override
	public boolean hasGrantedStore(Long storeId) {
		if(VelocitySecurityTool.hasStoreAuthority(storeId)){
			return Boolean.TRUE.booleanValue();
		}
		
		throw new UnauthenticatedException(String.format("has not permission of operating store[%s]", storeId));

	}

	@Override
	public boolean isSuperAdministrator() {
		return VelocitySecurityTool.isSuperAdmin();
	}

	@Override
	public Collection<Long> getGrantedStores() {
		try {
			return VelocitySecurityTool.getGrantedAuthorieStores();
		} catch (AuthenticationNotFoundException e) {
			throw new AuthenticatedException("not found authentication", e);
		} catch (AdminAccountException e) {
			throw new AuthenticatedException("user is a super admin", e);
		}
	}

}
