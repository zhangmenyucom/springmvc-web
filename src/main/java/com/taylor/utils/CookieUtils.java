package com.taylor.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.druid.util.StringUtils;

/**
 * @author HaydenWang
 *
 */
public class CookieUtils {

	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		if (cookieName == null || request == null) {
			return null;
		}
		Cookie[] cks = request.getCookies();
		if (cks == null) {
			return null;
		}
		for (Cookie cookie : cks) {
			if (cookieName.equals(cookie.getName()))
				return cookie.getValue();
		}
		return null;
	}

	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		if (cookieName == null || request == null) {
			return null;
		}
		Cookie[] cks = request.getCookies();
		if (cks == null) {
			return null;
		}
		for (Cookie cookie : cks) {
			if (cookieName.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	public static Cookie[] getCookies(HttpServletRequest request, String cookieName) {
		if (cookieName == null || request == null) {
			return null;
		}
		Cookie[] cks = request.getCookies();
		if (cks == null) {
			return null;
		}
		List<Cookie> cookies = new ArrayList<Cookie>();
		for (Cookie cookie : cks) {
			if (cookieName.equals(cookie.getName())) {
				cookies.add(cookie);
			}
		}
		return cookies.toArray(new Cookie[cookies.size()]);
	}

	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if (!StringUtils.isEmpty(domain)) {
			cookie.setDomain(domain);
		}
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}

	public static void addCookie(HttpServletResponse response, String name, String value, String path, int maxAge, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath(path);
		if (!StringUtils.isEmpty(domain)) {
			cookie.setDomain(domain);
		}
		cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
}
