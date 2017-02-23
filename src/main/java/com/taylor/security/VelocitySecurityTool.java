package com.taylor.security;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.weimob.common.web.security.MerchantUserAuthenticationToken;
import com.weimob.common.web.security.constants.SecurityAuthConstants;
import com.weimob.common.web.security.exceptions.AdminAccountException;
import com.weimob.common.web.security.exceptions.AuthenticationNotFoundException;
import com.weimob.common.web.util.HierarchyPatternMatcher;
import com.weimob.utility.util.StringUtils;

/**
 * Velocity 权限认证工具
 * 该工具的使用建立在spring Security基础上
 * 通过SecurityContextHolder.getContext()获取用户权限
 * 
 * @author Miao
 *
 */
@Log4j2
public class VelocitySecurityTool {
	
	/**
	 * 当前用户是否拥有授权码
	 * @param resourceCode
	 * 			授权码
	 * @return
	 */
	public static boolean hasResourceCode(String resourceCode){
		try {
			Set<String> codes = 
					getGrantedAuthorieCodes();
			return hasResourceCode(codes, resourceCode);
		} catch (AuthenticationNotFoundException e) {
			return false;
		} catch (AdminAccountException e) {
			return true;
		}
		
	}
	/**
	 * 当前用户是否拥有任意一个授权码
	 * @param resourceCode
	 * 			授权码
	 * @return
	 */
	public static boolean hasAnyResourceCode(String... resourceCodes){
		try {
			Set<String> codes = 
					getGrantedAuthorieCodes();
			return hasAnyResourceCode(codes, resourceCodes);
		} catch (AuthenticationNotFoundException e) {
			return false;
		} catch (AdminAccountException e) {
			return true;
		}
		
	}
	/**
	 * 当前用户是否拥有任意一个授权码
	 * @param resourceCode
	 * 			授权码
	 * @return
	 */
	public static boolean hasAnyResourceCode(Set<String> codes ,String... resourceCodes){
		if(resourceCodes == null || resourceCodes.length == 0){
			return true;
		}
		for (String resourceCode : resourceCodes) {
			if((!StringUtils.isEmpty(resourceCode))
					&& hasResourceCode(codes, resourceCode)){
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * 当前用户是否拥有授权码
	 * @param resourceCode
	 * 			授权码
	 * @return
	 */
	public static boolean hasResourceCode(Set<String> codes, String resourceCode){
		if(codes.contains(resourceCode)){
			return true;
		}
		for (String code : codes) {
			if(HierarchyPatternMatcher.match(code, 
					resourceCode, SecurityAuthConstants.USER_AUTH_CODE_SPLITTER)){
				return true;
			}
		}
		return false;
		
	}
	/**
	 * 是否是超级管理员
	 * @return
	 */
	public static boolean isSuperAdmin(){
		MerchantUserAuthenticationToken authentication = 
				(MerchantUserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null){
			log.error("not found authentication");
			return false;
		}
		return authentication.isSuperAdmin() && authentication.isSuperStoreAdmin();
	}
	
	/**
	 * 获取用户已授权权限
	 * @return
	 * @throws AuthenticationNotFoundException 
	 * @throws AdminAccountException 
	 */
	@Deprecated
	public static Set<String> getGrantedAuthories() throws AuthenticationNotFoundException, AdminAccountException{
		return getGrantedAuthorieCodes();
	}

	/**
	 * 获取用户已授权权限码
	 * @return
	 * @throws AuthenticationNotFoundException 
	 * @throws AdminAccountException 
	 */
	public static Set<String> getGrantedAuthorieCodes() throws AuthenticationNotFoundException, AdminAccountException{
		MerchantUserAuthenticationToken authentication = 
				(MerchantUserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null){
			throw new AuthenticationNotFoundException();
		}
//		if(authentication.isSuperAdmin()){
//			throw new AdminAccountException();
//		}
		Set<String> codes = 
				new HashSet<String>();
		if(authentication.getAuthorities()!=null 
			&& authentication.getAuthorities().size() > 0){
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				codes.add(authority.getAuthority());
			}
		}
		return codes;
	}

	/**
	 * 获取用户已授权门店
	 * @return
	 * @throws AuthenticationNotFoundException 
	 * @throws AdminAccountException 
	 */
	public static Set<Long> getGrantedAuthorieStores() throws AuthenticationNotFoundException, AdminAccountException{
		MerchantUserAuthenticationToken authentication = 
				(MerchantUserAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication == null){
			throw new AuthenticationNotFoundException();
		}
//		if(authentication.isSuperAdmin()){
//			throw new AdminAccountException();
//		}
		if(authentication.isSuperStoreAdmin()){
			throw new AdminAccountException();
		}
		Set<Long> codes = 
				new HashSet<Long>();
		if(authentication.getAuthorityStores()!=null 
			&& authentication.getAuthorityStores().size() > 0){
			for (GrantedAuthority authority : authentication.getAuthorityStores()) {
				codes.add(StringUtils.parseLong(authority.getAuthority()));
			}
		}
		return codes;
	}
	
	/**
	 * 当前用户是否拥有门店权限
	 * @param storeId
	 * 			门店id
	 * @return
	 */
	public static boolean hasStoreAuthority(Long storeId){
		try {
			if(storeId == null){
				return Boolean.FALSE.booleanValue();
			}
			Set<Long> authStoreIds = 
					getGrantedAuthorieStores();
			return hasStoreAuthority(authStoreIds, storeId);
		} catch (AuthenticationNotFoundException e) {
			log.error("not found authentication");
			return Boolean.FALSE.booleanValue();
		} catch (AdminAccountException e) {
			return Boolean.TRUE.booleanValue();
		}
		
	}
	
	/**
	 * 当前用户是否拥有门店权限
	 * @param authStoreIds
	 * @param storeId
	 * @return
	 */
	protected static boolean hasStoreAuthority(Set<Long> authStoreIds,
			Long storeId) {
		if(authStoreIds == null){
			return Boolean.FALSE.booleanValue();
		}
		return authStoreIds.contains(storeId);
	}
}
