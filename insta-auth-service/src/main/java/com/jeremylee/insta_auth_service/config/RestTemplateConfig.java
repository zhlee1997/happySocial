package com.jeremylee.insta_auth_service.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
public class RestTemplateConfig {

    @Bean
    @LoadBalanced // This enables service name resolution
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}
