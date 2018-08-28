package com.example.web.controller;

import com.example.mysql.entity.Department;
import com.example.mysql.entity.Role;
import com.example.mysql.entity.User;
import com.example.mysql.repository.DepartmentRepository;
import com.example.mysql.repository.RoleRepository;
import com.example.mysql.repository.UserRepository;
import com.example.web.service.ImageCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * created by liuxv on 2018/08/22
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@Controller
public class LoginController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private DepartmentRepository departmentRepository;


    @RequestMapping("/login")
    public String login(){

        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        //user.setPassword(bpe.encode(user.getPassword()));
        System.out.println( bpe.encode( "1234" ) );
               return "login";
    }


    @RequestMapping(value = "/images/imagecode")
    public String imagecode(HttpServletRequest request, HttpServletResponse response) throws Exception{
        OutputStream os=response.getOutputStream();
        //获取生成的图片编码
        Map<String,Object> map= ImageCode.getImageCode( 60,20,os );

        String simpleCaptcha="simpleCaptcha";

        request.getSession().setAttribute( simpleCaptcha,map.get( "strEnsure" ).toString().toLowerCase() );
        request.getSession().setAttribute( "codeTime",new Date(  ).getTime() );

        try{
            ImageIO.write( (BufferedImage) map.get( "image" ),"JPEG",os );
        }catch (IOException e){
            return "";
        }

        return null;

    }

    @RequestMapping(value = "/checkcode")
    @ResponseBody
    public String checkcode(HttpServletRequest request, HttpSession session) throws Exception{
        String checkCode=request.getParameter( "checkCode" );
        Object cko=session.getAttribute( "simpleCaptcha" );

        if(cko==null){
            request.setAttribute( "errorMsg","验证码已经失效，请重新输入!" );
            return "验证码已经失效，请重新输入！";
        }

        String captcha=cko.toString();
        Date now=new Date(  );

        Long codeTime=Long.valueOf( session.getAttribute( "codeTime" )+"" );

        if(StringUtils.isEmpty( checkCode )||captcha==null||!(checkCode.equalsIgnoreCase( captcha ))){
            request.setAttribute( "eooroMsg","验证码错误！" );
            return "验证码错误！";
        }else if((now.getTime()-codeTime)/1000/60>5){//验证码只有五分钟有效时间
            request.setAttribute( "errorMsg","验证码已失效，请重新输入！" );
            return "验证码已经失效，请重新输入！";
        }else{
            session.removeAttribute( "simpleCaptcha" );
            return "1";
        }



    }





}
