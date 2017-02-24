package com.taylor.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

public class AntPathRequestMatcher implements RequestMatcher{
	
    private final String pattern;
    private final HttpMethod httpMethod;
    
    public AntPathRequestMatcher(String pattern){
    	this(pattern, null);
    }

    public AntPathRequestMatcher(String pattern,String httpMethod){
        this.pattern = pattern;
        this.httpMethod = StringUtils.hasText(httpMethod) ? HttpMethod.valueOf(httpMethod) : null;
    }
    

	@Override
	public boolean matches(HttpServletRequest request) {
       return true;
    }

}
