package com.example.web.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

/**
 * created by liuxv on 2018/08/22
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
/*
权限管理的关键部分就是决断器，它实现了spring security的AccessDecisionManager，重载了decide函数，
使用了自定义的决断管理。
在用户访问收到保护的资源时候，决断器判断用户拥有的角色中是否对改资源具有访问权限，
如果没有权限将被拒绝访问，并返回错误提示。
 */
public class CustomAccessDecisionManager implements AccessDecisionManager {
    private static final Logger logger=LoggerFactory.getLogger( CustomAccessDecisionManager.class );


    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes==null){
            return;
        }

        Iterator<ConfigAttribute> iterator=configAttributes.iterator();

        while (iterator.hasNext()){
            ConfigAttribute configAttribute=iterator.next();

            String needRole=configAttribute.getAttribute();

            for (GrantedAuthority ga:authentication.getAuthorities()){
                if(needRole.equals( ga.getAuthority() )){
                    return;
                }
            }
            logger.info( "need role is :"+needRole );
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
