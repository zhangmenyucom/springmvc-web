package com.weimob.o2o.security.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.weimob.accounts.mgr.dao.mapper.vo.ResourceQueryBean;
import com.weimob.accounts.mgr.service.RbacResourceSetService;
import com.weimob.common.constants.IndustryEnum;
import com.weimob.common.weimobApi.WeimobApiService;
import lombok.extern.log4j.Log4j2;

import com.weimob.accounts.mgr.dao.mapper.vo.UserResourceQueryBean;
import com.weimob.accounts.mgr.domain.Resource;
import com.weimob.accounts.mgr.manager.AccountManager;
import com.weimob.common.response.CommonResponse;
import com.weimob.common.web.security.auth.UserAuthService;
import com.weimob.common.web.security.constants.SecurityAuthConstants;
import com.weimob.o2o.mgr.service.AccountStoreRelService;
import com.weimob.utility.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static com.weimob.accounts.mgr.manager.support.AuthSupport.isContainIndustry;

@Log4j2
public class UserAuthServiceImpl implements UserAuthService{

	@Autowired
	private WeimobApiService weimobApiService;

	private AccountManager accountManager; 

	private AccountStoreRelService accountStoreRelService;

	private RbacResourceSetService resourceSetService;
	
	private final static String DEFAULT_RESOURCE_CODE = "user";

	private final static long DEFAULT_STORE_ID = 0l;

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> loadUserAuthCodes(int sysCode,String merchantId, Long userId,String casInfoKey) {

		IndustryEnum industryEnum = weimobApiService.getIndustryByBid(Long.valueOf(merchantId));
		boolean isAdmin = (null == userId);

		if (isAdmin) {
			return loadAdminUserAuthCodes(industryEnum, sysCode);
		}

		return loadUserAuthCodesByParentId(industryEnum, sysCode, merchantId, userId, casInfoKey);
	}

	@Deprecated
	private Set<String> loadUserAuthCodes(IndustryEnum industryEnum, int sysCode, String merchantId, Long userId, String casInfoKey) {
		UserResourceQueryBean userResourceQueryBean =
				new UserResourceQueryBean(sysCode, merchantId, userId);
		CommonResponse<List<Resource>> resourcesResponse = accountManager.getUserResources(userResourceQueryBean);

		if(resourcesResponse == null || resourcesResponse.getData() == null){
			//TODO 异常
		}

		List<Resource> resources = resourcesResponse.getData();

		Set<String> authCodes = new HashSet<String>(resources == null ? 0 : resources.size());

		if(resources != null){
			for (Resource resource : resources) {
				if(StringUtils.isNotEmpty(resource.getResourceCode()) && isContainIndustry(resource.getIndustry(), industryEnum)){
					authCodes.addAll(
							StringUtils.tokenizeStringToList(
									resource.getResourceCode(),
									SecurityAuthConstants.AUTH_CODE_SPLITTER, true, true));
				}
			}
		}

		Set<String> adminAuthCodes = loadAdminUserAuthCodes(industryEnum, sysCode);

		Set<String> filteredAuthCodes = new HashSet<String>();
		for (String auth: authCodes) {
			if (adminAuthCodes.contains(auth)) {
				filteredAuthCodes.add(auth);
			}
		}

		filteredAuthCodes.add(DEFAULT_RESOURCE_CODE);

		return filteredAuthCodes;
	}

	private Set<String> loadUserAuthCodesByParentId(IndustryEnum industryEnum, int sysCode, String merchantId, Long userId, String casInfoKey) {
		UserResourceQueryBean userResourceQueryBean =
				new UserResourceQueryBean(sysCode, merchantId, userId);
		CommonResponse<List<Resource>> resourcesResponse = accountManager.getUserResources(userResourceQueryBean);

		if(resourcesResponse == null || resourcesResponse.getData() == null){
			//TODO 异常
		}

		List<Resource> resources = resourcesResponse.getData();

		List<Resource> filteredResources = new ArrayList<Resource>();
		if(resources != null){
			for (Resource resource : resources) {
				if (isContainIndustry(resource.getIndustry(), industryEnum)) {
					filteredResources.add(resource);
				}
			}
		}

		Set<String> filteredAuthCodes = new HashSet<String>();

		ResourceQueryBean queryBean = new ResourceQueryBean();
		queryBean.setBelongSys(sysCode);
		queryBean.setIsAssign(false);
		List<Resource> resourceSets = resourceSetService.selectByCondition(queryBean);

		for (Resource resourceSet : resourceSets) {
			if (StringUtils.isNotEmpty(resourceSet.getResourceCode())
					&& isContainIndustry(resourceSet.getIndustry(), industryEnum)) {
				for (Resource resource: filteredResources) {
					if (resource.getId().equals(resourceSet.getParentId())) {
						filteredAuthCodes.add(resourceSet.getResourceCode());
						break;
					}
				}
			}
		}

		filteredAuthCodes.add(DEFAULT_RESOURCE_CODE);

		return filteredAuthCodes;
	}

	private Set<String> loadAdminUserAuthCodes(IndustryEnum industryEnum, int sysCode) {
		ResourceQueryBean queryBean = new ResourceQueryBean();
		queryBean.setBelongSys(sysCode);
		queryBean.setIsAssign(false);
		List<Resource> resources = resourceSetService.selectByCondition(queryBean);

		Set<String> authCodes = new HashSet<String>(resources == null ? 0 : resources.size());

		if(resources != null){
			for (Resource resource : resources) {
				if(StringUtils.isNotEmpty(resource.getResourceCode()) && isContainIndustry(resource.getIndustry(), industryEnum)){
					authCodes.add(resource.getResourceCode());
				}
			}
		}

		authCodes.add(DEFAULT_RESOURCE_CODE);

		return authCodes;
	}
	
	public Collection<Long> loadUserAuthStores(int sysCode, String merchantId,
			Long userId, String casInfoKey) {
		List<Long> userAuthStores = null;
		try {
			userAuthStores = 
					accountStoreRelService.getStoreIdsByAccountId(userId);
			if(userAuthStores == null){
				log.warn("get user auto stores collection object is null");
			}
		} catch (Exception e) {
			log.error(String.format("load user auth stores failed.user account id is %s", userId), e);
		}
		
		if(userAuthStores == null){
			userAuthStores = new ArrayList<Long>();
		}
		userAuthStores.add(DEFAULT_STORE_ID);
		
		return userAuthStores;
	}

	public AccountManager getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public AccountStoreRelService getAccountStoreRelService() {
		return accountStoreRelService;
	}

	public void setAccountStoreRelService(
			AccountStoreRelService accountStoreRelService) {
		this.accountStoreRelService = accountStoreRelService;
	}

	public void setResourceSetService(RbacResourceSetService resourceSetService) {
		this.resourceSetService = resourceSetService;
	}
}
