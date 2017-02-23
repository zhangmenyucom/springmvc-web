package com.taylor.security;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSON;
import com.weimob.common.cache.redis.client.WeimobRedisSimpleClient;
import com.weimob.common.web.controller.BaseControllor;
import com.weimob.common.web.security.MerchantUserAuthenticationToken;
import com.weimob.common.web.util.CookieUtils;
import com.weimob.common.web.vo.CasInfo;
import com.weimob.common.weimobApi.WeimobApiService;
import com.weimob.utility.util.Log4j2Util;

@Log4j2
public class AuthenticationCheckFilter implements Filter{
	private static final String LOGIN_PAGE_PATH = "/login/index";
	private static final String SESSION_ID_COOKIE = "osessionid";
	private static final String STORE_ERROR_PAGE_PATH = "/store/submerchantErrorPage";

	private static final String MERCHANT_AUTHENTICATION_PATH = "/merchant/0/authorization";
	private static final String PARENT_RELOAD_PATH = "/reloadparent";
	private static final int REDIS_SESSION_TIME_SECONDS = 2 * 60 * 60;
	private static final int O2O_SYSTEM_CODE = 1;

	private Set<String> wechatExcludePath;
	
	/**
	 * 授权管理url
	 * 可以是http://o2o.weimob.com/merchant/0/authorization
	 * 也可以是/merchant/0/authorization
	 * 推荐使用全路径
	 */
	private String merchantAuthUrl = MERCHANT_AUTHENTICATION_PATH;
	
	/**
	 * 选择公众号号后，重新加载页面url
	 * 可以是http://o2o.weimob.com/reloadparent
	 * 也可以是/reloadparent
	 * 推荐使用全路径
	 */
	private String reloadUrl = PARENT_RELOAD_PATH;
	
	/**
	 * 商户id 缓存有效时间
	 */
	private int merchantIdSessionSeconds = REDIS_SESSION_TIME_SECONDS;
	
	private Long interval;

	private String redisClientBeanName;

	private WeimobRedisSimpleClient redisClient;

    private AuthenticationManager authenticationManager;

    private String loginUrl;
    
	/**
	 * 系统编号
	 */
	private int sysCode = O2O_SYSTEM_CODE;
	
    public AuthenticationCheckFilter() {
    }
    
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		log.info("*****filter****init****start*****");
		interval = com.weimob.utility.util.StringUtils.parseLong(filterConfig.getInitParameter("interval"));
		redisClientBeanName = filterConfig.getInitParameter("redisClientBeanName");

		ApplicationContext app = WebApplicationContextUtils
				.getWebApplicationContext(filterConfig.getServletContext());
		
		if(!com.weimob.utility.util.StringUtils.isEmptyOrNull(redisClientBeanName)){
			log.warn("redisClientBeanName param is empty");
			redisClient = (WeimobRedisSimpleClient) app
					.getBean(redisClientBeanName);
			
		}
		log.info("*****filter****init****end*****");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String loginRedirectPage = getLoginPageRedirectUrl(httpRequest);
		
		String path = httpRequest.getServletPath();
//		log.info("request path" + path);
		
		String sessionCookieValue = CookieUtils.getCookieValue(httpRequest, SESSION_ID_COOKIE);
		if(StringUtils.isEmpty(sessionCookieValue)) {
			clearCookies(httpRequest, httpResponse);
			httpResponse.sendRedirect(loginRedirectPage);
			return;
		}
		CasInfo casInfo = getCasInfoFromRedis(httpRequest, sessionCookieValue);
		if(casInfo == null) {
			clearCookies(httpRequest, httpResponse);
			httpResponse.sendRedirect(loginRedirectPage);
			return;
		}
		
		if(checkSessionExpired(casInfo)) {
			clearCookies(httpRequest, httpResponse);
			clearSession(sessionCookieValue);
			httpResponse.sendRedirect(loginRedirectPage);
			return;
		} else {
			resetSessionData(sessionCookieValue, casInfo);
		}

		// 判断是否有公众号
		if (isNotInwechatExcludePath(httpRequest.getRequestURI())) {
			if(casInfo.getAccountId() <= 0) {
				//未选择公众号
				//获取 WeimobApiService 实例
				WebApplicationContext cont = WebApplicationContextUtils.getRequiredWebApplicationContext(((HttpServletRequest) request).getSession().getServletContext());
				WeimobApiService weimobApiService = (WeimobApiService) cont.getBean("weimobApiServiceImpl");

				Long merchantId = weimobApiService.getFirstMerchantIdByBid(casInfo.getBid());
				if(merchantId == null || merchantId == 0){
					log.warn("cannot get merchantId,bid {},request path {}" ,casInfo.getBid(), path);
				}
				if (merchantId != null && merchantId > 0) {
					//默认选择一个公众号，重新加载页面
					casInfo.setAccountId(merchantId);
					casInfo.setTime(new Date().getTime());
					String casInfoJson = JSON.toJSONString(casInfo);
					redisClient.set(sessionCookieValue, casInfoJson);
					redisClient.expire(sessionCookieValue, this.merchantIdSessionSeconds);
					httpResponse.sendRedirect(getParentReloadRedirectUrl(httpRequest));
				} else {
					//未绑定公众号，跳转到授权管理页面
					httpResponse.sendRedirect(getMerchantAuthenticationRedirectUrl(httpRequest));
					return;
				}
			}
			
			String total = redisClient.get(String.format("o2o_store_total_%d",casInfo.getAccountId()));
			if(casInfo.getAccountSuiteInfo()!=null&&casInfo.getAccountSuiteInfo().isStoreLimit()
					&&casInfo.getUserId()!=0&&total != null&&Integer.valueOf(total) > casInfo.getAccountSuiteInfo().getStoreNum()) {
				httpResponse.sendRedirect(getRedirectUrl(httpRequest,STORE_ERROR_PAGE_PATH));
				return;
			}
			
		}
		
		
		//加载用户权限
		setAuthentication(casInfo, sessionCookieValue);
		
		chain.doFilter(httpRequest, httpResponse);
		
	}

//	private Long getWeChatAccountMerchantId(Long bid) {
//		Long merchantId = 0L;
//
//		String url = "http://www.dev.weimob.com/exapp/getAccountListBybId?bid=%s";
//		ObjectNode response = HTTPClientUtils.sendHTTPRequest(
//				String.format(url, bid), null,null, HttpMethod.POST);
//		String code = response.get("code").asText();
//		if ("0".equals(code)) {
//			JsonNode data = response.get("data");
//			int count = 0;
//			JsonNode tempWeChatAccount = data.get(count);
//			while (tempWeChatAccount != null && tempWeChatAccount.get("is_bp").asInt() != 1) {
//				count++;
//				tempWeChatAccount = data.get(count);
//			}
//			if (tempWeChatAccount != null) {
//				merchantId = tempWeChatAccount.get("id").asLong();
//			}
//		} else {
//			log.error("****** exception of get weChatAccounts from weimob");
//			log.error("****** bid : " + bid);
//			log.error("****** response : " + response);
//		}
//		return merchantId;
//	}
	

	private String getMerchantAuthenticationRedirectUrl(HttpServletRequest httpRequest) {
		return getRedirectUrl(httpRequest, this.merchantAuthUrl);
	}

	private String getParentReloadRedirectUrl(HttpServletRequest httpRequest) {
		return getRedirectUrl(httpRequest, this.reloadUrl);
	}

	private String getRedirectUrl(HttpServletRequest httpRequest, String urlPath) {
		if(com.weimob.utility.util.StringUtils.isHttpUrl(urlPath)){
			log.info(urlPath);
			return urlPath;
		}
		String scheme = httpRequest.getScheme();
		String serverName = httpRequest.getServerName();
		int port = httpRequest.getServerPort();
		String contextPath = httpRequest.getContextPath();
		String loginRedirectPagePrefix = scheme + "://" + serverName + ":" + port + contextPath;
		String loginRedirectPage = loginRedirectPagePrefix + urlPath;
		log.info(loginRedirectPage);
		return loginRedirectPage;
	}
	
	private void setAuthentication(CasInfo casInfo,String osessionId){
		if(authenticationManager == null){
			log.warn("authenticationManager in authentication check filter is null");
			return;
		}
		Long userId = casInfo.getUserId();
		
		Object principal = null;
		Object credentials = null;
		MerchantUserAuthenticationToken authentication = 
				new MerchantUserAuthenticationToken(principal, credentials
						,casInfo.getBid()+"",userId);
		authentication.setSuperAdmin(casInfo.isSuperAdmin());
		authentication.setSuperStoreAdmin(casInfo.isSuperAdmin());
		authentication.setSysCode(sysCode);
		authentication.setOsessionId(osessionId);
		// set the authentication into the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(
        		authenticationManager.authenticate(authentication));

	}
	
	private String getLoginPageRedirectUrl(HttpServletRequest httpRequest) {
		if(!StringUtils.isEmpty(loginUrl)){
			return loginUrl;
		}
		String scheme = httpRequest.getScheme();
		String serverName = httpRequest.getServerName();
		int port = httpRequest.getServerPort();
		String contextPath = httpRequest.getContextPath();
		String loginRedirectPagePrefix = scheme + "://" + serverName + ":" + port + contextPath;
		String loginRedirectPage = String.format(LOGIN_PAGE_PATH, loginRedirectPagePrefix);
		return loginRedirectPage;
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
		Cookie[] sessionCookies = CookieUtils.getCookies(request, SESSION_ID_COOKIE);
		if(sessionCookies != null) {
        	Log4j2Util.debug(log, "has got cookies {}", sessionCookies == null ? 0: sessionCookies.length);
			for(Cookie sessionCookie : sessionCookies){
				sessionCookie.setMaxAge(0);
				response.addCookie(sessionCookie);
			}
		}
	}

	private boolean checkSessionExpired(CasInfo casInfo) {
		boolean expired = false;
		long now = System.currentTimeMillis();
		long previous = casInfo.getTime();
		if(now - previous > interval) {
			expired = true;
		}
		return expired;
	}


	private CasInfo getCasInfoFromRedis(HttpServletRequest httpRequest, String key) {
		String infoStr = redisClient.get(key);
		if(StringUtils.isEmpty(infoStr)) {
			return null;
		}
		CasInfo casInfo = JSON.parseObject(infoStr, CasInfo.class);
		return casInfo;
	}

	private boolean isInWechatExcludePath(String url) {
		if (url == null) {
			throw new NullPointerException();
		}
		if (CollectionUtils.isNotEmpty(wechatExcludePath)) {
			for(String path : wechatExcludePath) {
				if (path != null) {
					if(url.matches(path)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isNotInwechatExcludePath(String url) {
		return !isInWechatExcludePath(url);
	}

	@Override
	public void destroy() {
		this.authenticationManager = null;
	}
	public Long getInterval() {
		return interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}

	public WeimobRedisSimpleClient getRedisClient() {
		return redisClient;
	}

	public void setRedisClient(WeimobRedisSimpleClient redisClient) {
		this.redisClient = redisClient;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public int getSysCode() {
		return sysCode;
	}

	public void setSysCode(int sysCode) {
		this.sysCode = sysCode;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getMerchantAuthUrl() {
		return merchantAuthUrl;
	}

	public void setMerchantAuthUrl(String merchantAuthUrl) {
		this.merchantAuthUrl = merchantAuthUrl;
	}

	public String getReloadUrl() {
		return reloadUrl;
	}

	public void setReloadUrl(String reloadUrl) {
		this.reloadUrl = reloadUrl;
	}

	public int getMerchantIdSessionSeconds() {
		return merchantIdSessionSeconds;
	}

	public void setMerchantIdSessionSeconds(int merchantIdSessionSeconds) {
		this.merchantIdSessionSeconds = merchantIdSessionSeconds;
	}

	public void setWechatExcludePath(Set<String> wechatExcludePath) {
		this.wechatExcludePath = wechatExcludePath;
	}
}
