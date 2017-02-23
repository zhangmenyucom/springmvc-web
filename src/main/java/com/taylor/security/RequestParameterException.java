/**
 * 
 */
package com.taylor.security;

/**
 * 请求参数异常
 */
public class RequestParameterException extends RuntimeException {

	private static final long serialVersionUID = -6903499033552865610L;

	/**
	 * 
	 */
	public RequestParameterException() {
		super();
	}

	/**
	 * @param message
	 */
	public RequestParameterException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public RequestParameterException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RequestParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public RequestParameterException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
