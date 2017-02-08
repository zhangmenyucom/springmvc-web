package com.taylor.resolver;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

import com.taylor.annotation.ApiAuthInfo;
import com.taylor.common.AuthResult;
import com.taylor.utils.CookieUtils;

public class ApiAuthInfoMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final int OPEN_ID_COOKIE_EXPIRE_TIME = 30 * 24 * 60 * 60;


	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ApiAuthInfo.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		ApiAuthInfo annotation = parameter.getParameterAnnotation(ApiAuthInfo.class);
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		
		ApiAuthInfo.UidFrom uidFrom = annotation.uidFrom();
		String name = annotation.name();
		String uid = null;
		switch (uidFrom) {
			case Path:
				Map<String, String> uriTemplateVariables = getUriTemplateVariables(webRequest);
				uid = uriTemplateVariables.get(name);
				break;
			case Parameter:
				uid = request.getParameter(name);
				break;
			case Cookie:
				uid = CookieUtils.getCookieValue(request,name);
				break;
			default:
				break;
		}
		if (null == uid) {
			if (annotation.required()) {
				throw new MissingServletRequestParameterException(name, Long.class.getName());
			} else {
				return null;
			}
		}else{
			CookieUtils.addCookie(response, name, uid, OPEN_ID_COOKIE_EXPIRE_TIME,null);
		}
		return AuthResult.builder().uid(uid).build();
	}

	protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
		@SuppressWarnings("unchecked")
		Map<String, String> variables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		return (variables != null ? variables : Collections.<String, String> emptyMap());
	}

}
