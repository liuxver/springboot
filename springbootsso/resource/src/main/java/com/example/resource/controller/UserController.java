package com.example.resource.controller;

import com.example.mysql.entity.User;
import com.example.mysql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * created by liuxv on 2018/08/27
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/user")
    public Map<String,Object> user(Principal puser){
        User user=userRepository.findByName( puser.getName() );
        Map<String,Object> userinfo=new HashMap<>(  );
        userinfo.put( "id",user.getId() );
        userinfo.put("name",user.getName());
        userinfo.put("email", user.getEmail());
        userinfo.put("department",user.getDepartment().getName());
        userinfo.put("createdate", user.getCreatedate());
        return userinfo;
    }
}
