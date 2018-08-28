package com.example.web.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


import java.util.*;


/**
 * created by liuxv on 2018/08/21
 * email:liuxver444@gmail.com
 * qq:1369058574
 */

/*
权限配置资源管理器  实现了spring security的FilterInvocationSecurityMetadataSource，
它在启动的时候导入application.yml的权限配置列表。
权限配置资源管理器为权限决断器实时提供支持，，判断用户访问的资源是否在受保护的范围之内。

 */
public class CustomSecurityMetadateSource implements FilterInvocationSecurityMetadataSource {
    private static final Logger logger= LoggerFactory.getLogger( CustomSecurityMetadateSource.class );

    private Map<String,Collection<ConfigAttribute>> resourceMap=null;
    private PathMatcher pathMatcher=new AntPathMatcher(  );

    private String urlroles;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String url=((FilterInvocation)object).getRequestUrl();

        logger.debug( "request url is "+url );

        if(resourceMap==null)
            resourceMap=loadResourceMatchAuthority();

        Iterator<String> ite=resourceMap.keySet().iterator();

        while (ite.hasNext()){
            String resURL=ite.next();
            if(pathMatcher.match( resURL,url )){
                return resourceMap.get( resURL );
            }
        }
        return resourceMap.get( url );
    }


    public CustomSecurityMetadateSource(String urlroles){
        super();
        this.urlroles=urlroles;
        resourceMap=loadResourceMatchAuthority();
    }

    private Map<String,Collection<ConfigAttribute>> loadResourceMatchAuthority(){
        Map<String,Collection<ConfigAttribute>> map=new HashMap<>(  );

        if(urlroles!=null&& !urlroles.isEmpty()){
            String[] resources=urlroles.split( ";" );
            for (String resource:resources){
                String[] urls=resource.split( "=" );
                String[] roles=urls[1].split( "," );
                Collection<ConfigAttribute> list=new ArrayList<>(  );
                for (String role:roles){
                    ConfigAttribute configAttribute=new SecurityConfig( role.trim() );
                    list.add( configAttribute );
                }

                map.put( urls[0].trim(),list );
            }
        }else{
            logger.error( "'secourityconfig.urlroles' must be set!"  );
        }

        logger.info( "Loaded UrlRoles Resources." );
        return map;
    }



    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
