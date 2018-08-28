package com.example.security;


import org.apache.log4j.Logger;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.*;
import java.io.IOException;


/**
 * created by liuxv on 2018/08/27
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
public class CustomFilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter{
    private static final Logger logger=Logger.getLogger( CustomFilterSecurityInterceptor.class );
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation filterInvocation=new FilterInvocation( servletRequest,servletResponse,filterChain );
        logger.debug( "===="+filterInvocation.getRequestUrl() );
        invoke(filterInvocation);
    }

    public void invoke(FilterInvocation filterInvocation) throws IOException,ServletException{
        InterceptorStatusToken token=super.beforeInvocation( filterInvocation );
        try{
            filterInvocation.getChain().doFilter( filterInvocation.getRequest(),filterInvocation.getResponse() );
        }catch (Exception e){
            logger.error( e.getMessage() );
        }finally {
            super.afterInvocation( token,null );
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }
}
