package com.example.web.service;

import com.example.mysql.entity.Role;
import com.example.mysql.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * created by liuxv on 2018/08/21
 * email:liuxver444@gmail.com
 * qq:1369058574
 */


/*
SecurityUser继承于实体对象User，并实现了Spring Security的UserDetails
重载了getAuthorities()，用来取得为用户分配的角色列表，用于后面 的权限验证
 */
public class SecurityUser extends User implements UserDetails{
    private static final long serialVersionUID=1L;

    public SecurityUser(User user){
        if(user!=null){
            this.setId( user.getId() );
            this.setName( user.getName() );
            this.setEmail( user.getEmail() );
            this.setPassword( user.getPassword() );
            this.setSex( user.getSex() );
            this.setCreatedate( user.getCreatedate() );
            this.setRoles( user.getRoles() );
        }
    }


    //用来取得为用户分配的角色列表，用于后面 的权限验证
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        Collection<GrantedAuthority> authorities=new ArrayList<>(  );
        List<Role> roles=this.getRoles();
        if(roles!=null){
            for(Role role:roles){
                SimpleGrantedAuthority authority=new SimpleGrantedAuthority( role.getName() );
                authorities.add( authority );
            }
        }
        return authorities;
    }

    @Override
    public String getPassword(){
        return super.getPassword();
    }
    @Override
    public String getUsername() {
        return super.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
