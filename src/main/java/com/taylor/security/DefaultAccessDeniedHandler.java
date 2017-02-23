package com.weimob.common.web.security.exceptionhandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 用户资源访问拒绝处理器
 * @author Miao
 *
 */
public class DefaultAccessDeniedHandler implements AccessDeniedHandler{

	/**
	 * 重定向url
	 * 不包含ContextPath
	 */
	private String accessDeniedUrl;

	public String getAccessDeniedUrl() {
		return accessDeniedUrl;
	}

	public void setAccessDeniedUrl(String accessDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
	}

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {
		response.sendRedirect(request.getContextPath()+accessDeniedUrl);
	}

}
