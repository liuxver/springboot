package com.example.web.service;

import com.example.mysql.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * created by liuxv on 2018/08/21
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    protected Log log= LogFactory.getLog( getClass() );





    /*
    登录成功的处理器 ，简单输出用户登录的日志
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException,ServletException{
        User userDetails=(User)authentication.getPrincipal();

        log.info( "登录用户user："+userDetails.getName()+"  login "+request.getContextPath() );
        log.info( "IP: "+getIpAddress(request) );

        super.onAuthenticationSuccess( request,response,authentication );
    }

    public String getIpAddress(HttpServletRequest request){
        String ip=request.getHeader( "x-forwarded-for" );

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }



}
