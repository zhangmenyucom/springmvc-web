package com.taylor.security;

import com.alibaba.fastjson.JSON;
import com.weimob.common.cache.redis.client.WeimobRedisSimpleClient;
import com.weimob.common.response.SessionInvalidResponse;
import com.weimob.common.web.controller.BaseControllor;
import com.weimob.common.web.util.CookieUtils;
import com.weimob.common.web.util.ResponseUtils;
import com.weimob.common.web.vo.CasInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by bruce on 2016/10/21.
 */
public class ApiSessionCheckInterceptor extends HandlerInterceptorAdapter {

    private static final String SESSION_ID_COOKIE = "osessionid";
    private static final int SESSION_EXPIRED_SECONDS = 2 * 60 * 60;
    private static final int SESSION_INVALID_CODE = 99887766;
    private static final String LOGIN_PAGE = "/login/index";

    @Qualifier("weimobSimpleRedisClient")
    @Autowired
    private WeimobRedisSimpleClient redisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String sessionCookieValue = CookieUtils.getCookieValue(request, SESSION_ID_COOKIE);
        if(StringUtils.isEmpty(sessionCookieValue)) {
            clearCookies(request, response);
            ResponseUtils.response(response, getSessionInvalidResponse());
            return false;
        }
        CasInfo casInfo = getCasInfoFromRedis(request, sessionCookieValue);
        if(casInfo == null) {
            clearCookies(request, response);
            ResponseUtils.response(response, getSessionInvalidResponse());
            return false;
        }

        if(checkSessionExpired(casInfo)) {
            clearCookies(request, response);
            clearSession(sessionCookieValue);
            ResponseUtils.response(response, getSessionInvalidResponse());
            return false;
        } else {
            resetSessionData(sessionCookieValue, casInfo);
        }

        return true;
    }

    private String getSessionInvalidResponse() {
        return JSON.toJSONString(new SessionInvalidResponse(SESSION_INVALID_CODE,"session invalid, please login in",LOGIN_PAGE));
    }

    private void resetSessionData(String key, CasInfo casInfo) {
        casInfo.setTime(System.currentTimeMillis());
        String casInfoJson = JSON.toJSONString(casInfo);
        redisClient.set(key, casInfoJson);
        redisClient.expire(key, 2 * 60 * 60);
        redisClient.expire(key + BaseControllor.SOURCE_KEY, 2 * 60 * 60);

    }

    private void clearSession(String key) {
        if(redisClient.exists(key)) {
            redisClient.del(key);
            redisClient.del(key + BaseControllor.SOURCE_KEY);
        }
    }

    private void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie sessionCookie = CookieUtils.getCookie(request, SESSION_ID_COOKIE);
        if(sessionCookie != null) {
            sessionCookie.setMaxAge(0);
            response.addCookie(sessionCookie);
        }
    }

    private boolean checkSessionExpired(CasInfo casInfo) {
        boolean expired = false;
        long now = System.currentTimeMillis();
        long previous = casInfo.getTime();
        if(now - previous > SESSION_EXPIRED_SECONDS * 1000) {
            expired = true;
        }
        return expired;
    }


    private CasInfo getCasInfoFromRedis(HttpServletRequest request, String key) {
        String infoStr = redisClient.get(key);
        if(StringUtils.isEmpty(infoStr)) {
            return null;
        }
        CasInfo casInfo = JSON.parseObject(infoStr, CasInfo.class);
        return casInfo;
    }
}
