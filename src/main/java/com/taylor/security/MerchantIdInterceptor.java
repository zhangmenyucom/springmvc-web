package com.taylor.security;

import com.alibaba.fastjson.JSON;
import com.weimob.common.cache.redis.client.WeimobRedisSimpleClient;
import com.weimob.common.exceptions.ManagerException;
import com.weimob.common.web.util.CookieUtils;
import com.weimob.common.web.vo.CasInfo;
import com.weimob.utility.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by lenovo on 2016/4/21.
 */

@Log4j2
public class MerchantIdInterceptor implements HandlerInterceptor{


    @Autowired
    @Qualifier(value="weimobSimpleRedisClient")
    protected WeimobRedisSimpleClient redisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //从URL中获取merchantId
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if(pathVariables == null) {
            return true;
        }
        Long urlMerchantId = Long.parseLong(pathVariables.get("merchantId").toString());
        //获取CasInfo
        String sessionCookieValue = CookieUtils.getCookieValue(request, "osessionid");
        if(StringUtils.isEmptyOrNull(sessionCookieValue)){
            return false;
        }
        String casInfoJson = redisClient.get(sessionCookieValue);
        CasInfo casInfo = null;
        if(!StringUtils.isEmpty(casInfoJson)) {
            casInfo = JSON.parseObject(casInfoJson, CasInfo.class);
        }
        //缓存中的merchantId
        long merchantId = casInfo.getAccountId();
        //如果缓存中的merchantId等于url中的merchantId 则合法
        if(urlMerchantId == merchantId){
            return true;
        }else{
            throw new ManagerException(-1,"商户无访问权限！");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
