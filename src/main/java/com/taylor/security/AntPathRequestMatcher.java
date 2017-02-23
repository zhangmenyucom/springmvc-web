package com.taylor.security;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.log4j.Log4j2;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import com.weimob.common.web.util.PathPatternMatcher;
import com.weimob.common.web.util.RequestUtil;

@Log4j2
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
        if (httpMethod != null && request.getMethod() != null && httpMethod != HttpMethod.valueOf(request.getMethod())) {
        	/*if (log.isDebugEnabled()) {
                log.debug("Request '" + request.getMethod() + " " + RequestUtil.getRequestPath(request) + "'"
                        + " doesn't match '" + httpMethod  + " " + pattern);
            }*/
            return false;
        }

        String url = RequestUtil.getRequestPath(request);

        /*if (log.isDebugEnabled()) {
            log.debug("Checking match of request : '" + url + "'; against '" + pattern + "'");
        }*/

        return PathPatternMatcher.match(pattern, url);
    }

}
