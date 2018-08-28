package com.example.web.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.*;
import java.io.IOException;

/**
 * created by liuxv on 2018/08/21
 * email:liuxver444@gmail.com
 * qq:1369058574
 */

/*
权限管理过滤器 继承于Spring Security的AbstractSecurityInterceptor，实时监控用户的行为，
防止用户访问未被授权的资源
 */
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
    private static final Logger logger= LoggerFactory.getLogger( CustomFilterSecurityInterceptor.class );
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,ServletException{
        FilterInvocation fi=new FilterInvocation( request,response,chain );
        logger.debug( "===="+fi.getRequestUrl() );
        invoke(fi);
    }

    @Override
    public void destroy() {

    }


    public void invoke(FilterInvocation fi) throws IOException,ServletException{
        InterceptorStatusToken token=super.beforeInvocation( fi );
        try{
            fi.getChain().doFilter( fi.getRequest(),fi.getResponse() );
        }catch (Exception e){
            logger.error( e.getMessage() );
        }finally {
            super.afterInvocation( token,null );
        }
    }


    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return securityMetadataSource;
    }

    @Override
    public Class<? extends Object> getSecureObjectClass(){
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource(){
        return this.securityMetadataSource;
    }

    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }
}
