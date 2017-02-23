package com.weimob.common.web.security.notification.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.weimob.common.cache.redis.client.WeimobRedisSimpleClient;
import com.weimob.common.web.security.notification.AuthUpdateNotification;

@Component(value = "cacheAuthUpdateNotification")
public class AuthUpdateNotificationImpl implements AuthUpdateNotification{

	private final static String UPDATE_AUTH_KEY = "UPDATE_AUTH:%s:%s:%s";

	private static final int USER_SESSION_TIME_SECONDS = 30 * 60;
	
	@Autowired
	@Qualifier(value = "weimobSimpleRedisClient")
	private WeimobRedisSimpleClient redisClient;

	@Override
	public void notifyAuthUpdated(int sysCode,String merchantId, Long userId, 
			Object notificationContent) {
		if(notificationContent == null){
			throw new IllegalArgumentException("notificationContent arg must be not null");
		}
		String updateFlagKey = getUpdateFlagKey(sysCode, merchantId, userId);
		// 用户权限已修改标志
		this.redisClient.set(updateFlagKey, notificationContent);
		this.redisClient.expire(updateFlagKey, USER_SESSION_TIME_SECONDS);
	}

	@Override
	public Object readNotification(int sysCode,String merchantId, Long userId) {
		String updateFlagKey = getUpdateFlagKey(sysCode, merchantId, userId);
		// 用户权限已修改标志
		Object updateFlag = this.redisClient.getObject(updateFlagKey);
		return updateFlag;
	}

	@Override
	public void deleteNotification(int sysCode,String merchantId, Long userId) {
		//清空权限更新标志
		String updateFlagKey = getUpdateFlagKey(sysCode, merchantId, userId);
		this.redisClient.del(updateFlagKey);
	}

	private String getUpdateFlagKey(int sysCode,String merchantId, Long userId){
		return String.format(UPDATE_AUTH_KEY,
				sysCode,
				merchantId,
				userId);
	}
	
}
