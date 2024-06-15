package com.niksob.gateway_service.config.security.web.filter.token.access;

import com.niksob.gateway_service.config.security.context.SecurityContextRepository;
import com.niksob.gateway_service.security.web.auth.converter.AccessTokenAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
public class AccessTokenFilterConfig {
    @Bean("accessTokenFilter")
    public AuthenticationWebFilter getAccessTokenFilter(
            ReactiveAuthenticationManager authenticationManager,
            AccessTokenAuthenticationConverter accessTokenAuthenticationConverter,
            SecurityContextRepository securityContextRepository
    ) {
        final AuthenticationWebFilter accessTokenFilter = new AuthenticationWebFilter(authenticationManager);
        accessTokenFilter.setServerAuthenticationConverter(accessTokenAuthenticationConverter);
        accessTokenFilter.setSecurityContextRepository(securityContextRepository);
        return accessTokenFilter;
    }
}
