/**
 * 
 */
package com.taylor.security;

/**
 * 未授权异常
 */
public class UnauthenticatedException extends RuntimeException {

	private static final long serialVersionUID = -6903499033552865610L;

	/**
	 * 
	 */
	public UnauthenticatedException() {
		super();
	}

	/**
	 * @param message
	 */
	public UnauthenticatedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnauthenticatedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnauthenticatedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnauthenticatedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
