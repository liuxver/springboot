package com.example.login.config;

import com.example.login.service.CustomUserDetailsService;
import com.example.login.service.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.sql.DataSource;

/**
 * created by liuxv on 2018/08/24
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
        authenticationManagerBuilder.userDetailsService( customUserDetailsService ).passwordEncoder( passwordEncoder() );
        authenticationManagerBuilder.eraseCredentials( false );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.formLogin().loginPage( "/login" ).permitAll().successHandler( loginSuccessHandler() )
                .and().authorizeRequests()
                .antMatchers( "/images/**","/checkcode","/scripts/**","/styles/**" ).permitAll()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy( SessionCreationPolicy.NEVER )
                .and().exceptionHandling().accessDeniedPage( "/deny" )
                .and().rememberMe().tokenValiditySeconds( 86400 ).tokenRepository( tokenRepository() );

    }






    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(  );
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler();
    }



    @Bean
    public JdbcTokenRepositoryImpl tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository=new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource( dataSource );
        return jdbcTokenRepository;
    }



}
