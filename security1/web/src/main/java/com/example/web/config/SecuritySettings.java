package com.example.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * created by liuxv on 2018/08/21
 * email:liuxver444@gmail.com
 * qq:1369058574
 */


/*
这是一个自定义的配置类，从 application.yml 文件里面读取前缀为  securityconfig 的配置参数
logoutsuccessurl：定义退出成功的链接
permitall：定义允许访问的URL列表
deniedpage：定义拒绝访问的信息提示链接
urlroles：定义一个权限管理规则，是链接地址与角色权限的配置列表



其中，urlroles 配置一个权限配置列表，是我配置的一种规则：列表中的每一个配置项用分号分离，
每一个配置项左边是一个可以带上通配符的链接地址，等号右边是一个角色列表，角色之间用逗号分离。
每一个配置项表示包含等号左边字符串的链接地址都能够被等号右边的角色访问。

权限配置完成后，要让系统中的哪一个用户具有哪些权限，只要分配给这个用户一些角色就可以了。
 */
@ConfigurationProperties(prefix = "securityconfig")
public class SecuritySettings {
    private String logoutsuccessurl="/logout";
    private String permitall="/api";
    private String deniedpage="/deny";
    private String urlroles;


    public String getLogoutsuccessurl() {
        return logoutsuccessurl;
    }

    public void setLogoutsuccessurl(String logoutsuccessurl) {
        this.logoutsuccessurl = logoutsuccessurl;
    }

    public String getPermitall() {
        return permitall;
    }

    public void setPermitall(String permitall) {
        this.permitall = permitall;
    }

    public String getDeniedpage() {
        return deniedpage;
    }

    public void setDeniedpage(String deniedpage) {
        this.deniedpage = deniedpage;
    }

    public String getUrlroles() {
        return urlroles;
    }

    public void setUrlroles(String urlroles) {
        this.urlroles = urlroles;
    }
}
