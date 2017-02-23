package com.taylor.security;

import java.util.Map;
import java.util.Set;


/**
 * 用户权限缓存接口
 * 
 * @author Miao.Xiong
 *
 */
public interface UserAuthCacheService extends UserAuthService{

	public final static String USER_AUTH_DETAILS = "USER_AUTH_DETAILS";

	/**
	 * 用户授权门店 变量名
	 */
	public final static String USER_AUTH_STORES = "USER_AUTH_STORES";

	/**
	 * 检查用户授权码缓存是否有效
	 * @param merchantId
	 * @param accountId
	 * @return
	 */
	public boolean checkValidation(int sysCode,String merchantId,Long userId);
	
	/**
	 * 将用户授权码放入缓存
	 * @param merchantId
	 * @param accountId
	 * @param values
	 * @return
	 */
	public boolean putCache(int sysCode,String merchantId,Long userId,
			String casInfoKey,Map<String, Object> values);
	
}
