package com.taylor.security;

import java.util.Collection;

/**
 * 授权接口
 * @author miao
 *
 */
public interface GrantedAuthenticationService {
	
	/**
	 * 是否授权过门店
	 * @param storeId
	 * @return
	 */
	public boolean hasGrantedStore(Long storeId);
	
	/**
	 * 是否是超级管理员
	 * @return
	 */
	public boolean isSuperAdministrator();
	
	/**
	 * 获取授权门店
	 * @return
	 */
	public Collection<Long> getGrantedStores();
	
}
