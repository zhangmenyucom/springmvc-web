/**
 * 
 */
package com.taylor.security;

/**
 * 微信授权失败异常
 */
public class WechatAuthenticatedFailedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4672251562850717820L;

	/**
	 * 
	 */
	public WechatAuthenticatedFailedException() {
		super();
	}

	/**
	 * @param message
	 */
	public WechatAuthenticatedFailedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public WechatAuthenticatedFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public WechatAuthenticatedFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public WechatAuthenticatedFailedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
