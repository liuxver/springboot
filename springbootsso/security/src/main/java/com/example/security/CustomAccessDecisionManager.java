package com.example.security;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * created by liuxv on 2018/08/27
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
public class CustomAccessDecisionManager implements AccessDecisionManager {
    private static final Logger logger=Logger.getLogger( CustomAccessDecisionManager.class );


    //判断是否有权限
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes==null){
            return;
        }

        //config urlroles
        Iterator<ConfigAttribute> iterator=configAttributes.iterator();

        while (iterator.hasNext()){
            ConfigAttribute configAttribute=iterator.next();
            //需要的角色
            String needRole=configAttribute.getAttribute();
            //用户的角色
            for(GrantedAuthority ga:authentication.getAuthorities()){
                if (needRole.equals( ga.getAuthority() )){
                    return;
                }
            }
            logger.info( "need role is  : "+needRole );

        }
        throw new AccessDeniedException( "Cannot Access!" );

    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
