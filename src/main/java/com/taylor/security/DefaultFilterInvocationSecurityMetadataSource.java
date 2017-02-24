package com.taylor.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class DefaultFilterInvocationSecurityMetadataSource  implements FilterInvocationSecurityMetadataSource {
	private final Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    //~ Constructors ===================================================================================================

    /**
     * Sets the internal request map from the supplied map. The key elements should be of type {@link RequestMatcher},
     * which. The path stored in the key will depend on
     * the type of the supplied UrlMatcher.
     *
     * @param requestMap order-preserving map of request definitions to attribute lists
     */
    public DefaultFilterInvocationSecurityMetadataSource(
            LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {

        if(requestMap == null){
        	requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        }
        
        this.requestMap = requestMap;
    }

    //~ Methods ========================================================================================================

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    public Collection<ConfigAttribute> getAttributes(Object object) {
        final HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        Collection<ConfigAttribute> returnCollection = new ArrayList<ConfigAttribute>(); 
        returnCollection.add(new SecurityConfig("user")); 
        return returnCollection;
    }

    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
    
}
