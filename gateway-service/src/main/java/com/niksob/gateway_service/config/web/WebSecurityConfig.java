package com.niksob.gateway_service.config.web;

import com.niksob.gateway_service.path.controller.signup.LoginControllerPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    private final ReactiveUserDetailsService userDetailsService;

    //    private final AccessTokenFilter accessTokenFilter;

    @Value("${microservice.connection.gateway.path}")
    private String basePath;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        final String loginControllerPath = basePath + LoginControllerPaths.BASE_URI;
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                loginControllerPath
                        ).permitAll()
                        .anyExchange().authenticated())
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для инициации выхода из системы
                        .logoutSuccessHandler((webFilterExchange, authentication) -> {
                            webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
                            return webFilterExchange.getExchange().getResponse().setComplete();
                        }));
        http.authenticationManager(authenticationManager());
        return http.build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }
}
