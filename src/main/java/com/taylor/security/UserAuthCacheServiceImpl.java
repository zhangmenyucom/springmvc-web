package com.taylor.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.weimob.common.cache.redis.client.WeimobRedisSimpleClient;
import com.weimob.common.web.security.auth.UserAuthCacheService;
import com.weimob.common.web.security.notification.AuthUpdateNotification;
import com.weimob.common.web.util.CasInfoUtils;
import com.weimob.common.web.vo.CasInfo;
import com.weimob.utility.util.StringUtils;

@Component("weimobSimpleUserAuthCacheService")
public class UserAuthCacheServiceImpl implements UserAuthCacheService{
	
	private final static String CODE_SPLIT =",";

	@Autowired
	@Qualifier(value = "weimobSimpleRedisClient")
	private WeimobRedisSimpleClient redisClient;
	
	@Autowired
	private AuthUpdateNotification authUpdateNotification;

	@Override
	public Collection<String> loadUserAuthCodes(int sysCode,String merchantId, Long userId,
			String casInfoKey) {
		
		CasInfo casInfo = getCaseInfo(sysCode,merchantId, userId,casInfoKey);
		String cacheUserAuthCodes =  (String) casInfo.getExtraValues().get(USER_AUTH_DETAILS);
		if(StringUtils.isEmpty(cacheUserAuthCodes)){
			return Collections.emptyList();
		}
		
		@SuppressWarnings("unchecked")
		List<String> authCodes = 
				StringUtils.tokenizeStringToList(cacheUserAuthCodes,
						CODE_SPLIT, false, true);
		return authCodes;
	}
	
	private CasInfo getCaseInfo(int sysCode,String merchantId, Long userId,
			String casInfoKey){

		CasInfo casInfo = CasInfoUtils.getCasInfo(this.redisClient, casInfoKey);
		if(casInfo == null){
			throw new UsernameNotFoundException(String.format("merchantId %s,userId %s not found", 
					merchantId,userId));
		}
		return casInfo;
	}

	@Override
	public boolean checkValidation(int sysCode,String merchantId, Long userId) {
		// 用户权限已修改标志
		Object updateFlag = 
				authUpdateNotification.readNotification(sysCode,merchantId, userId);
		
		//缓存中UpdateFlag值不存在即表示 用户授权码缓存是有效的
		return updateFlag == null;
	}

	@Override
	public boolean putCache(int sysCode,String merchantId, Long userId,
			String casInfoKey,
			Map<String,Object> values) {
		//放入缓存
		CasInfo casInfo = getCaseInfo(sysCode,merchantId, userId,casInfoKey);
		casInfo.getExtraValues().putAll(values);
		this.redisClient.set(casInfoKey,
				JSON.toJSONString(casInfo));
		
		//删除权限变更通知
		authUpdateNotification.deleteNotification(sysCode, merchantId, userId);
		return true;
	}

	@Override
	public Collection<Long> loadUserAuthStores(int sysCode, String merchantId,
			Long userId, String casInfoKey) {
		CasInfo casInfo = getCaseInfo(sysCode,merchantId, userId,casInfoKey);
		String cacheUserAuthStores =  (String) casInfo.getExtraValues().get(USER_AUTH_STORES);
		if(StringUtils.isEmpty(cacheUserAuthStores)){
			return Collections.emptyList();
		}
		
		
		List<Long> authStores = 
				StringUtils.tokenizeToLongList(cacheUserAuthStores, CODE_SPLIT);
		return authStores;
	}

}
