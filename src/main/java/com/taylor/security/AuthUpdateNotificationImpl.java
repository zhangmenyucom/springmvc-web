package com.taylor.security;

import org.springframework.stereotype.Component;

@Component(value = "cacheAuthUpdateNotification")
public class AuthUpdateNotificationImpl implements AuthUpdateNotification{

	@Override
	public void notifyAuthUpdated(int sysCode, String merchantId, Long userId, Object notificationContent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object readNotification(int sysCode, String merchantId, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteNotification(int sysCode, String merchantId, Long userId) {
		// TODO Auto-generated method stub
		
	}

	
}
