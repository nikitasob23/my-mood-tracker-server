package com.niksob.gateway_service.config.security.web;

import com.niksob.gateway_service.path.controller.auth.token.AuthTokenControllerPaths;
import com.niksob.gateway_service.path.controller.signup.LoginControllerPaths;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    private final ReactiveAuthenticationManager authenticationManager;

    @Setter
    @Qualifier("accessTokenFilter")
    private AuthenticationWebFilter accessTokenFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                LoginControllerPaths.BASE_URI + "/**",
                                AuthTokenControllerPaths.BASE_URI + "/**"
                        ).permitAll()
                        .anyExchange().authenticated())
                .addFilterAt(accessTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для инициации выхода из системы
                        .logoutSuccessHandler((webFilterExchange, authentication) -> {
                            webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
                            return webFilterExchange.getExchange().getResponse().setComplete();
                        }));
        http.authenticationManager(authenticationManager);
        return http.build();
    }
}
