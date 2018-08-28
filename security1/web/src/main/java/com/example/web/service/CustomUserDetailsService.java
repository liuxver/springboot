package com.example.web.service;

import com.example.mysql.entity.User;
import com.example.mysql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * created by liuxv on 2018/08/21
 * email:liuxver444@gmail.com
 * qq:1369058574
 */


/*
CustomUserDetailsService实现了spring security的UserDetailsService，重载了loadUserByUsername（String userName），
并且返回自定义的SecurityUser，通过这个SecurityUser来完成用户的身份认证。
其中，loadUserByUserName调用了用户资源库接口的findByName方法，取得用户的 详细信息。

 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user=userRepository.findByName( username );

        if(user==null){
            throw new UsernameNotFoundException( "UserName "+username+" not found." );
        }

        return new SecurityUser(user);
    }
}
