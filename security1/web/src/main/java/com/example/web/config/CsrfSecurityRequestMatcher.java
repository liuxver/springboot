package com.example.web.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

/**
 * created by liuxv on 2018/08/21
 * email:liuxver444@gmail.com
 * qq:1369058574
 */


/*
需要对第三方开接口的时候，这个时候，需要对特定的URL使用派出CSRF保护的方法来实现。
 */
public class CsrfSecurityRequestMatcher implements RequestMatcher {
    protected Log log= LogFactory.getLog( getClass() );

    private Pattern allowedMethods=Pattern.compile( "^(GET|HEAD|TRACE|OPTIONS)$" );

    /**
     * 需要排除的url列表
     */
    private List<String> execludeUrls;


    //判断request请求是否在排除的url列表之内
    @Override
    public boolean matches(HttpServletRequest request){
        if(execludeUrls!=null&&execludeUrls.size()>0){
            String servletPath=request.getServletPath();
            for (String url:execludeUrls){
                if (servletPath.contains( url )){
                    log.info( "++++"+servletPath );
                    return false;
                }
            }
        }
        return !allowedMethods.matcher( request.getMethod() ).matches();
    }

    public List<String> getExecludeUrls() {
        return execludeUrls;
    }

    public void setExecludeUrls(List<String> execludeUrls) {
        this.execludeUrls = execludeUrls;
    }
}
