package com.niksob.gateway_service.config.security.web.filter.token.access;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
public class AccessTokenFilterConfig {
    @Bean("accessTokenFilter")
    public AuthenticationWebFilter getAccessTokenFilter(ReactiveAuthenticationManager authenticationManager) {
        return new AuthenticationWebFilter(authenticationManager);
    }
}
