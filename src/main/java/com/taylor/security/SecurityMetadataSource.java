package com.weimob.o2o.security;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.weimob.accounts.common.constant.ResourceTypeEnum;
import com.weimob.accounts.mgr.dao.mapper.vo.ResourceQueryBean;
import com.weimob.accounts.mgr.domain.Resource;
import com.weimob.accounts.mgr.manager.ResourceManager;
import com.weimob.common.response.CommonResponse;
import com.weimob.common.web.security.matcher.AntPathRequestMatcher;
import com.weimob.o2o.common.Constant.SysNameConstants;
import com.weimob.utility.util.StringUtils;

/**
 * url请求权限定义
 * @author Miao.Xiong
 *
 */
@Log4j2
public class SecurityMetadataSource extends
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> {
	
	private static final long serialVersionUID = 7080311336748087995L;

	@Autowired
	private ResourceManager resourceManager;
	
	/**
	 * 是否可以使用.*匹配url
	 */
	private boolean useSuffixPatternMatch = true;  
	
	/**
	 * 是否可以使用/匹配url
	 * @unused
	 */
	private boolean useTrailingSlashMatch = true;

	/**
	 * 系统编号
	 */
	private int sysCode = SysNameConstants.SYS_O2O;
	
	@PostConstruct
	public void loadSecurityInfos(){
//		put("/xiong/test","test11111");
//		put("/xiong/test2","test2");
//		put("/xiong/test/*/123","test3");
//		加载所有url资源
		ResourceQueryBean resourceQueryBean = 
				new ResourceQueryBean(ResourceTypeEnum.URL.getType(), 
						SysNameConstants.SYS_O2O, null, Boolean.TRUE,null,null);
		CommonResponse<List<Resource>> resourcesResponse = 
				resourceManager.getResources(resourceQueryBean);
		if(resourcesResponse != null && resourcesResponse.getData() != null) {
			/**
			 * 解析url资源权限定义
			 */
			for (final Resource resource : resourcesResponse.getData()) {
				if(StringUtils.isEmpty(resource.getResourceUrl())
						|| StringUtils.isEmpty(resource.getResourceCode())){
					continue;
				}
				String url = resource.getResourceUrl().replaceAll("/+", "/");
				String pureUrl = getPureUrl(url);
				this.put(pureUrl, resource.getResourceCode());
//				if(this.useSuffixPatternMatch){
//					this.put(pureUrl + ".*", resource.getResourceCode());
//				}
//				if(this.useTrailingSlashMatch){
//					this.put(pureUrl + "/", resource.getResourceCode());
//				}
			}
		}else{
			log.error(String.format("加载自定义资源失败：%s", resourcesResponse.getMessage()));
			throw new RuntimeException();
		}
		
	}
	
	private String getPureUrl(String url){
		if(url.endsWith("/")){
			return url.substring(0, url.length() - 1);
		}else if(url.endsWith(".*")){
			return url.substring(0, url.length() - 2);
		}else{
			return url;
		}
	}
	private void put(String url,final String code){
		
		AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url);
		Collection<ConfigAttribute> configAttributes = new LinkedList<ConfigAttribute>();
		configAttributes.add(new ConfigAttribute() {
			private static final long serialVersionUID = 1L;

			public String getAttribute() {
				return code;
			}
		});	
		this.put(antPathMatcher, configAttributes);
	}

    public boolean isUseSuffixPatternMatch() {
		return useSuffixPatternMatch;
	}

	public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch) {
		this.useSuffixPatternMatch = useSuffixPatternMatch;
	}

	public boolean isUseTrailingSlashMatch() {
		return useTrailingSlashMatch;
	}

	public void setUseTrailingSlashMatch(boolean useTrailingSlashMatch) {
		this.useTrailingSlashMatch = useTrailingSlashMatch;
	}

	public int getSysCode() {
		return sysCode;
	}

	public void setSysCode(int sysCode) {
		this.sysCode = sysCode;
	}

}