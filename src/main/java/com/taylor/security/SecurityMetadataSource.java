package com.taylor.security;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * url请求权限定义
 * 
 * @author Miao.Xiong
 *
 */
public class SecurityMetadataSource extends LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> {/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 */  
	private static final long serialVersionUID = -3894936087383245111L;

}