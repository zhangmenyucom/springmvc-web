package com.taylor.security;

/**
 * 用户权限详细信息接口
 * @author Miao
 *
 */
public interface UserAuthDetailsService {

	public UserAuthDetails loadUserAuthDetails(int sysCode,String merchantId,Long accountId,
			String casInfoKey);
	
}
