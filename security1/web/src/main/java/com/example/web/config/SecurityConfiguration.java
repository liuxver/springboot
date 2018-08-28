package com.example.web.config;


import com.example.web.service.CustomUserDetailsService;


import com.example.web.service.LoginSuccessHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


/**
 * created by liuxv on 2018/08/20
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@EnableConfigurationProperties(SecuritySettings.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
    protected Log log = LogFactory.getLog(getClass());

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecuritySettings settings;


    //调用用户自定义的用户认证
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    //datasource指明要将信息存储在数据库中
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    //用户身份验证
    //调用自定义的用户认证customUserDetailsService，并且指定了官方推荐的安全加密算法BCryptPasswordEncoder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService( customUserDetailsService ).passwordEncoder( passwordEncoder() );

        auth.eraseCredentials( false );
    }

    //返回BCryptPasswordEncoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(  );
    }





    @Override
    protected void configure(HttpSecurity http) throws Exception{
        /*
        loginPage：设置一个使用自定义的登陆界面URL，如果不设定，将会调用自定义的登陆界面，很简单
        loginSuccessHandler：设置一个自定义的登录成功处理器
        permitAll：是完全允许访问的一些URL配置，并且可以使用通配符来设置，在这里将一些资源
              目录赋予可以完全访问的权限按，由settings指定的权限列表也赋予了完全访问的权限
          logout：设置使用设置的登出
          logoutSeccessUrl：设置登出成功的链接
          rememberMe：记住用户的登录状态 用户没有执行退出的时候，再次打开页面将不用登录
          csrf：跨站请求伪造（cross-site request forgery），这是一个防止跨站请求伪造攻击的策略设置
          accessDeniedPage：配置当抛出异常以后，一个拒绝访问的提示链接



          setting   引用了自定义的配置参数


         */
        http.formLogin().loginPage( "/login" ).permitAll().successHandler( loginSuccessHandler() )
                .and().authorizeRequests()
                .antMatchers( "/images/**","/checkcode","/scripts/**","/styles/**" ).permitAll()
                .antMatchers( settings.getPermitall().split( "," ) ).permitAll()
                .anyRequest().authenticated()
                .and().csrf().requireCsrfProtectionMatcher( csrfSecurityRequestMatcher() )
                .and().sessionManagement().sessionCreationPolicy( SessionCreationPolicy.NEVER )
                .and().logout().logoutSuccessUrl( settings.getLogoutsuccessurl() )
                .and().exceptionHandling().accessDeniedPage( settings.getDeniedpage() )
                .and().rememberMe().tokenValiditySeconds( 86400 ).tokenRepository( tokenRepository() );


    }



    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }



    //在这个配置类中，加入需要排除的CSRF攻击的链接列表，只要链接地址中半酣“/rest”，就
    //对其忽略CSRF保护策略
    private CsrfSecurityRequestMatcher csrfSecurityRequestMatcher(){
        CsrfSecurityRequestMatcher csrfSecurityRequestMatcher=new CsrfSecurityRequestMatcher();
        List<String> list=new ArrayList<>(  );
        list.add( "/rest/" );
        list.add( "/login/" );
        csrfSecurityRequestMatcher.setExecludeUrls( list );
        return csrfSecurityRequestMatcher;
    }




    //记住用户登录状态的配置，将用户信息的登录令牌存储在数据库中
    @Bean
    public JdbcTokenRepositoryImpl tokenRepository(){
        JdbcTokenRepositoryImpl jtr=new JdbcTokenRepositoryImpl();
        jtr.setDataSource( dataSource );
        return jtr;
    }









    //以下 是 没有在上面两个大函数里面用到的
    /*
    CustomFilterSecurityInterceptor:权限管理过滤器
    CustomAccessDecisionManager:权限管理决断器
    CustomSecurityMetadataSource:权限配置资源管理器
    其中，过滤器在系统启动的时候开始工作，并同时导入资源管理器和权限决断器，对用户访问的资源进行
  管理。权限决断器对用户访问的资源与用户拥有的角色权限进行对比，以此来判断一个用户是否对一个资源具有访问权限。

     */
    @Bean
    public CustomFilterSecurityInterceptor customFilter() throws Exception{
        CustomFilterSecurityInterceptor customFilterSecurityInterceptor=new CustomFilterSecurityInterceptor();
        customFilterSecurityInterceptor.setSecurityMetadataSource( securityMetadataSource() );

        customFilterSecurityInterceptor.setAccessDecisionManager( accessDecisionManager() );
        customFilterSecurityInterceptor.setAuthenticationManager( authenticationManager );

        return customFilterSecurityInterceptor;
    }


    @Bean
    public CustomSecurityMetadateSource securityMetadataSource(){
        return new CustomSecurityMetadateSource(settings.getUrlroles());
    }

    @Bean
    public CustomAccessDecisionManager accessDecisionManager(){
        return new CustomAccessDecisionManager();
    }




}
