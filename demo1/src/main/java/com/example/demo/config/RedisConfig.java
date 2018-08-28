package com.example.demo.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by liuxv on 2018/8/15
 * email:liuxver444@gmail.com
 * qq:1369058574
 */
@Configuration
public class RedisConfig extends CachingConfigurerSupport{
    @Bean
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory factory){

    }
}
