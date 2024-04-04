package com.niksob.gateway_service.config.security.authentication.manager;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

@Configuration
@AllArgsConstructor
public class AuthenticationManagerConfig {
    @Bean
    public ReactiveAuthenticationManager getAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }
}
