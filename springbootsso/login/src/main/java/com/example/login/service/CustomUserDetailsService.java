package com.example.login.service;

import com.example.mysql.entity.User;
import com.example.mysql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * created by liuxv on 2018/08/24
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
        User user=userRepository.findByName( userName );

        if (user==null){
            throw new UsernameNotFoundException( "UserName "+userName+" not found!" );
        }

        return new SecurityUser(user);

    }
}
