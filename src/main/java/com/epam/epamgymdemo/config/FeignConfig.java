package com.epam.epamgymdemo.config;

import com.epam.epamgymdemo.filter.JwtAuthFilter;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return  requestTemplate -> {
            String token = JwtAuthFilter.getCurrentToken();
            if (token != null) {
                requestTemplate.header("Authorization", "Bearer " + token);
            }
        };
    }
}
