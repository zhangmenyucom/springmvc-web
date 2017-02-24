package com.taylor.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * 用户权限决定管理器
 * 
 * @author Miao
 *
 */
public class DefaultAccessDecisionManager implements AccessDecisionManager {

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		if (configAttributes == null) {
			return;
		}

		MerchantUserAuthenticationToken merchantUserAuthenticationToken = (MerchantUserAuthenticationToken) authentication;
		Iterator<ConfigAttribute> iterator = configAttributes.iterator();

/*		while (iterator.hasNext()) {
			ConfigAttribute configAttribute = iterator.next();
			// 访问所请求资源所需要的权限
			String needPermission = configAttribute.getAttribute();
			// 用户所拥有的权限authentication
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if (HierarchyPatternMatcher.match(ga.getAuthority(), needPermission, SecurityAuthConstants.USER_AUTH_CODE_SPLITTER)) {
					return;
				}
			}

		}
*/
		throw new AccessDeniedException(" No Access Dendied ");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
