/**
 * 
 */
package com.taylor.common.utils;

/**
 * @author HaydenWang
 *
 */
public class UtilityRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UtilityRuntimeException() {
		super();
	}

	/**
	 * @param message
	 */
	public UtilityRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UtilityRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UtilityRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UtilityRuntimeException(String message, Throwable cause,
                                   boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
