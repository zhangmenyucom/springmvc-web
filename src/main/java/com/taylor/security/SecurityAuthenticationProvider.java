package com.taylor.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import lombok.extern.log4j.Log4j;

/**
 * 用户认证权限提供器
 * 
 * @author Miao.Xiong
 * 
 */
@Log4j
public class SecurityAuthenticationProvider implements AuthenticationProvider {

	private UserAuthDetailsService userAuthDetailsService;

	public UserAuthDetailsService getUserAuthDetailsService() {
		return userAuthDetailsService;
	}

	public void setUserAuthDetailsService(UserAuthDetailsService userAuthDetailsService) {
		this.userAuthDetailsService = userAuthDetailsService;
	}

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(MerchantUserAuthenticationToken.class, authentication, "Only MerchantUserAuthenticationToken is supported");

		MerchantUserAuthenticationToken authenticationToken = (MerchantUserAuthenticationToken) authentication;

		if (authenticationToken.getMerchantId() == null) {
			throw new MerchantAccountNotFoundException("merchantId is null");
		}
		if (!authenticationToken.isSuperAdmin() && authenticationToken.getUserId() == null) {
			throw new MerchantAccountNotFoundException("userId is null");
		}

		UserAuthDetails userAuthDetails = retrieveUserAuth(authenticationToken);

		Assert.notNull(userAuthDetails, "retrieve User auth details");

		return createSuccessAuthentication(authenticationToken.getPrincipal(), authenticationToken, userAuthDetails);
	}

	protected Authentication createSuccessAuthentication(Object principal, MerchantUserAuthenticationToken authentication, UserAuthDetails user) {
		MerchantUserAuthenticationToken result = new MerchantUserAuthenticationToken(user, authentication.getCredentials(), authentication.getMerchantId(), authentication.getUserId(), authoritiesMapper.mapAuthorities(user.getAuthorities()));
		result.setSuperAdmin(authentication.isSuperAdmin());
		result.setDetails(authentication.getDetails());
		result.setSuperStoreAdmin(authentication.isSuperStoreAdmin());
		result.setAuthorityStores(authoritiesMapper.mapAuthorities(user.getAuthorityStores()));

		return result;
	}

	/**
	 * 获取用户权限信息
	 * 
	 * @param authentication
	 * @return
	 * @throws UsernameNotFoundException
	 */
	private UserAuthDetails retrieveUserAuth(MerchantUserAuthenticationToken authentication) throws UsernameNotFoundException {
		UserAuthDetails user = userAuthDetailsService.loadUserAuthDetails(authentication.getSysCode(), authentication.getMerchantId(), (authentication.isSuperAdmin() ? null : authentication.getUserId()), authentication.getOsessionId());
		if (user == null) {
			log.debug("User AccountId'" + authentication.getUserId() + "' not found");
		}
		return user;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (MerchantUserAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
