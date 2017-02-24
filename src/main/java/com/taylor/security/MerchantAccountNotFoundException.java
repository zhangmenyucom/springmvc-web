package com.taylor.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Thrown if an authentication request is rejected because Merchant or account Id empty 
 * @author Miao
 *
 */
public class MerchantAccountNotFoundException extends AuthenticationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MerchantAccountNotFoundException(String msg) {
		super(msg);
	}

}
