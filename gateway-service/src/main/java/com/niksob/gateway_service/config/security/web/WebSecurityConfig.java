package com.niksob.gateway_service.config.security.web;

import com.niksob.gateway_service.config.security.context.SecurityContextRepository;
import com.niksob.domain.path.controller.gateway_service.AuthControllerPaths;
import com.niksob.gateway_service.path.controller.auth.token.AuthTokenControllerPaths;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@AllArgsConstructor
@EnableWebFluxSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final ReactiveAuthenticationManager authenticationManager;
    @Qualifier("accessTokenFilter")
    private final AuthenticationWebFilter accessTokenFilter;

    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                AuthControllerPaths.BASE_URI + AuthControllerPaths.SIGNUP + "/**",
                                AuthControllerPaths.BASE_URI + AuthControllerPaths.EMAIL_RESETTING_ACTIVATION + "/**",
                                AuthTokenControllerPaths.BASE_URI + "/**"
                        ).permitAll()
                        .anyExchange().authenticated())
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .addFilterAt(accessTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
