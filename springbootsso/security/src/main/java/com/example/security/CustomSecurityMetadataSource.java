package com.example.security;

import org.apache.log4j.Logger;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


import java.util.*;

/**
 * created by liuxv on 2018/08/27
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final Logger logger=Logger.getLogger( CustomSecurityMetadataSource.class );
    private Map<String,Collection<ConfigAttribute>> resourceMap=null;
    private PathMatcher pathMatcher=new AntPathMatcher(  );
    private String urlroles;



    public CustomSecurityMetadataSource(String urlroles){
        super();
        this.urlroles=urlroles;
        resourceMap=loadResourceMatchAuthority();

    }

    private Map<String,Collection<ConfigAttribute>> loadResourceMatchAuthority(){
        Map<String,Collection<ConfigAttribute>> map=new HashMap<>(  );
        if(urlroles!=null&& !urlroles.isEmpty()) {
            String[] resources = urlroles.split( ";" );
            for (String resource : resources) {
                String[] urls = resource.split( "=" );
                String[] roles = urls[1].split( "," );
                Collection<ConfigAttribute> list = new ArrayList<>();

                for (String role : roles) {
                    ConfigAttribute configAttribute = new SecurityConfig( role.trim() );
                    list.add( configAttribute );
                }
                map.put( urls[0].trim(),list );
            }
        }else {
            logger.error( "'securityconfig.urlroles ' myst be set!   （必须要在application.yml里面配置urlroles ） " );
        }

        logger.info( "Loaded UrlRoles Resources!" );
        return map;
    }







    //返回可以访问链接的角色
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String url=((FilterInvocation)object).getRequestUrl();
        logger.debug( "request url is "+url );

        if(resourceMap==null)
            resourceMap=loadResourceMatchAuthority();

        Iterator<String> iterator=resourceMap.keySet().iterator();

        while (iterator.hasNext()){
            String resURL=iterator.next();
            if(pathMatcher.match( resURL,url )){
                return resourceMap.get( resURL );
            }
        }

        return resourceMap.get( url );
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
