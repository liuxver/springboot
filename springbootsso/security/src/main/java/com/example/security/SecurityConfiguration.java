package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * created by liuxv on 2018/08/27
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@Configuration
@EnableOAuth2Sso
@EnableConfigurationProperties(SecuritySettings.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecuritySettings settings;

    @Override
    public void configure(HttpSecurity http) throws Exception{
        http.antMatcher( "/**" ).authorizeRequests()
                .antMatchers( settings.getPermitall().split( "," ) ).permitAll()
                .anyRequest().authenticated()
                .and().csrf().requireCsrfProtectionMatcher( csrfSecurityRequestMatcher() )
                .csrfTokenRepository( csrfTokenRepository() )
                .and().addFilterAfter( csrfHeaderFilter(), CsrfFilter.class )
                .logout().logoutUrl( "/logout" ).permitAll()
                .logoutSuccessUrl( settings.getLogoutsuccessurl() )
                .and().exceptionHandling().accessDeniedPage( settings.getDeniedpage());
    }

    @Bean
    public CustomFilterSecurityInterceptor customFilter() throws Exception{
        CustomFilterSecurityInterceptor customFilterSecurityInterceptor=new CustomFilterSecurityInterceptor();
        customFilterSecurityInterceptor.setSecurityMetadataSource( securityMetadataSource() );
        customFilterSecurityInterceptor.setAccessDecisionManager( accessDecisionManager() );
        customFilterSecurityInterceptor.setAuthenticationManager( authenticationManager );
        return customFilterSecurityInterceptor;
    }

    private CsrfSecurityRequestMatcher csrfSecurityRequestMatcher(){
        CsrfSecurityRequestMatcher csrfSecurityRequestMatcher=new CsrfSecurityRequestMatcher();
        List<String> list=new ArrayList<>(  );
        list.add( "/rest/" );
        csrfSecurityRequestMatcher.setExecludeUrls( list );
        return csrfSecurityRequestMatcher;
    }

    private CsrfTokenRepository csrfTokenRepository(){
        HttpSessionCsrfTokenRepository repository=new HttpSessionCsrfTokenRepository();
        repository.setHeaderName( "X-XSRF-TOKEN" );
        return repository;
    }


    private Filter csrfHeaderFilter(){
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
                CsrfToken csrfToken=(CsrfToken)httpServletRequest.getAttribute( CsrfToken.class.getName() );
                if(csrfToken!=null){
                    Cookie cookie=new Cookie( "XSRF-TOKEN",csrfToken.getToken() );
                    cookie.setPath( "/" );
                    httpServletResponse.addCookie( cookie );
                }
                filterChain.doFilter( httpServletRequest,httpServletResponse );
            }
        };
    }


    @Bean
    public CustomSecurityMetadataSource securityMetadataSource(){
        return new CustomSecurityMetadataSource(settings.getUrlroles());
    }

    @Bean
    public CustomAccessDecisionManager accessDecisionManager(){
        return new CustomAccessDecisionManager();
    }


}
