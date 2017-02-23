package com.taylor.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 用户认证权限token
 * @author Miao.Xiong
 *
 */

public class MerchantUserAuthenticationToken extends AbstractAuthenticationToken{

    private final Object principal;
    private Object credentials;
    private String merchantId;
    private Long userId;
    private int sysCode;
    private String osessionId;
    
    private boolean isSuperAdmin;

	/**
	 * 是否是门店超级管理员
	 * 临时字段
	 */
	private boolean isSuperStoreAdmin;

	/**
	 * 授权门店
	 */
	private Collection<? extends GrantedAuthority> authorityStores;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MerchantUserAuthenticationToken(Object principal, Object credentials,String merchantId,Long userId) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.merchantId = merchantId;
        this.userId = userId;
        setAuthenticated(false);
    }
	public MerchantUserAuthenticationToken(Object principal, Object credentials
			,String merchantId,Long userId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.merchantId = merchantId;
        this.userId = userId;
        super.setAuthenticated(true); // must use super, as we override
    }

	public MerchantUserAuthenticationToken(Object principal, Object credentials
			,String merchantId,Long userId, Collection<? extends GrantedAuthority> authorities
			, Collection<? extends GrantedAuthority> authorityStores) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.merchantId = merchantId;
        this.userId = userId;
        super.setAuthenticated(true); // must use super, as we override
        this.authorityStores = authorityStores;
    }


    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                "Once created you cannot set this token to authenticated. Create a new instance using the constructor which takes a GrantedAuthority list will mark this as authenticated.");
        }

        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }


	public String getMerchantId() {
		return merchantId;
	}


	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public int getSysCode() {
		return sysCode;
	}
	public void setSysCode(int sysCode) {
		this.sysCode = sysCode;
	}
	public String getOsessionId() {
		return osessionId;
	}
	public void setOsessionId(String osessionId) {
		this.osessionId = osessionId;
	}
	public boolean isSuperStoreAdmin() {
		return isSuperStoreAdmin;
	}
	public void setSuperStoreAdmin(boolean isSuperStoreAdmin) {
		this.isSuperStoreAdmin = isSuperStoreAdmin;
	}
	public Collection<? extends GrantedAuthority> getAuthorityStores() {
		return authorityStores;
	}
	public void setAuthorityStores(Collection<? extends GrantedAuthority> authorityStores) {
		this.authorityStores = authorityStores;
	}

}
