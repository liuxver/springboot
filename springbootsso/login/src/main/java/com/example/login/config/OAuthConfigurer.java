package com.example.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * created by liuxv on 2018/08/23
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@Configuration
@EnableAuthorizationServer //开启OAuth2的认证服务器功能
public class OAuthConfigurer extends AuthorizationServerConfigurerAdapter {


    /*
    使用生成的数字证书 keystore.jks ，并且设置了密码和别名等参数。
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter=new JwtAccessTokenConverter();
        KeyPair keyPair=new KeyStoreKeyFactory( new ClassPathResource( "keystore.jks" ),"123456".toCharArray() ).getKeyPair( "tycoonclient" );
        converter.setKeyPair( keyPair );
        return converter;
    }


    /*
    设定OAuth2的客户端ID为ssoclient，密钥为ssosecret  ，这将在SSO的客户端配置中使用到
    autoApprove(true) 这行代码设定了自动确认授权，这样的在用户登录后，不再需要进行一次授权确认操作。

     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception{
        clientDetailsServiceConfigurer.inMemory().withClient( "ssoclient" ).secret( "ssosecret" )
                .autoApprove( true )
                .authorizedGrantTypes( "authorization_code","refresh_token" ).scopes( "openid" );

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer serverSecurityConfigurer) throws Exception{
        serverSecurityConfigurer.tokenKeyAccess( "permitAll()" ).checkTokenAccess( "isAuthenticated()" ).allowFormAuthenticationForClients();

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer) throws Exception{
        endpointsConfigurer.accessTokenConverter( jwtAccessTokenConverter() );
    }

}
