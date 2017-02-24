package com.taylor.security;

/**
 * 用户权限变更通知
 * @author Miao
 *
 */
public interface AuthUpdateNotification {
	
	/**
	 * 权限变更公告
	 * 
	 * @param merchantId
	 * @param userId
	 * @param notificationContent
	 */
	public void notifyAuthUpdated(int sysCode,String merchantId, Long userId, Object notificationContent);
	
	/**
	 * 阅读权限变更通告
	 * @param merchantId
	 * @param userId
	 */
	public Object readNotification(int sysCode,String merchantId, Long userId);
	
	/**
	 * 删除权限变更通告
	 * @param merchantId
	 * @param userId
	 */
	public void deleteNotification(int sysCode,String merchantId, Long userId);
}
