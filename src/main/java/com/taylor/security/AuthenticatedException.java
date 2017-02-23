/**
 * 
 */
package com.taylor.security;

/**
 * 授权异常
 */
public class AuthenticatedException extends RuntimeException {

	private static final long serialVersionUID = -6903499033552865610L;

	/**
	 * 
	 */
	public AuthenticatedException() {
		super();
	}

	/**
	 * @param message
	 */
	public AuthenticatedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AuthenticatedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AuthenticatedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AuthenticatedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
