package com.taylor.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.weimob.common.web.security.auth.UserAuthCacheService;
import com.weimob.common.web.security.auth.UserAuthDetails;
import com.weimob.common.web.security.auth.UserAuthDetailsService;
import com.weimob.common.web.security.auth.UserAuthService;
import com.weimob.utility.util.StringUtils;

public class UserAuthDetailsServiceImpl implements UserAuthDetailsService{

	private UserAuthService userAuthService ;

	@Autowired
	@Qualifier(value = "weimobSimpleUserAuthCacheService")
	private UserAuthCacheService userAuthCacheService;

	@Override
	public UserAuthDetails loadUserAuthDetails(int sysCode,String merchantId, Long userId,
			String casInfoKey) {
		if(userAuthService == null){
			//TODO
		}
		Collection<String> cacheAuthCodes = null;
		Collection<Long> cacheAuthStores = null;
		//检验缓存中的数据是否有效
		boolean cacheIsValid = userAuthCacheService.checkValidation(sysCode,merchantId, userId);
		if((!cacheIsValid)){
			cacheAuthCodes = userAuthService.loadUserAuthCodes(sysCode,merchantId, userId,casInfoKey);
			cacheAuthStores = userAuthService.loadUserAuthStores(sysCode, merchantId, userId, casInfoKey);
			
			//放入缓存
			Map<String, Object> authorities = 
					new HashMap<String, Object>();
			authorities.put(UserAuthCacheService.USER_AUTH_DETAILS, StringUtils.collectionToStrings(cacheAuthCodes));
			authorities.put(UserAuthCacheService.USER_AUTH_STORES, StringUtils.collectionToStrings(cacheAuthStores));
			userAuthCacheService.putCache(sysCode,merchantId, userId,casInfoKey, authorities);
		}else{

			//从缓存中获取
			cacheAuthCodes = 
					userAuthCacheService.loadUserAuthCodes(sysCode,merchantId, userId,casInfoKey);
			
			cacheAuthStores = 
					userAuthCacheService.loadUserAuthStores(sysCode,merchantId, userId,casInfoKey);
		}
		Collection<GrantedAuthority> authorities = new LinkedList<GrantedAuthority>();
		for (String authCode : cacheAuthCodes) {
			authorities.add(new SimpleGrantedAuthority(authCode));
		}
		
		Collection<GrantedAuthority> authorityStores = new LinkedList<GrantedAuthority>();
		for (Long authStore : cacheAuthStores) {
			authorityStores.add(new SimpleGrantedAuthority(String.valueOf(authStore)));
		}
		
		UserAuthDetails userAuthDetails = 
				new UserAuthDetails(merchantId, userId, false, authorities, authorityStores);
		
		return userAuthDetails;
	}

	public UserAuthService getUserAuthService() {
		return userAuthService;
	}

	public void setUserAuthService(UserAuthService userAuthService) {
		this.userAuthService = userAuthService;
	}
}
