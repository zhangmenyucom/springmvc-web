package com.taylor.security;

import java.util.Collection;

/**
 * 用户权限获取接口
 * 
 * @author Miao.Xiong
 *
 */
public interface UserAuthService {

	/**
	 * 获取用户授权码
	 * @param merchantId
	 * @param accountId
	 * @return
	 */
	public Collection<String> loadUserAuthCodes(int sysCode,String merchantId,Long userId,
			String casInfoKey);

	/**
	 * 获取用户授权门店
	 * @param merchantId
	 * @param accountId
	 * @return
	 */
	public Collection<Long> loadUserAuthStores(int sysCode,String merchantId,Long userId,
			String casInfoKey);
}
