package com.niksob.gateway_service.security.web.auth.converter;

import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.gateway_service.model.token.authentication.AuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccessTokenAuthenticationConverter implements ServerAuthenticationConverter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";

    @Value("${microservice.connection.gateway.path}")
    private String basePath;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        final String path = basePath + exchange.getRequest().getPath().pathWithinApplication().value();

        return extractToken(exchange)
                .map(accessToken -> new AuthenticationToken(accessToken, path))
                .map(token -> new PreAuthenticatedAuthenticationToken(null, token));
    }

    private Mono<AccessToken> extractToken(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            final String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
            if (authHeader != null && authHeader.startsWith(BEARER)) {
                final String bearerValue = authHeader.substring(7);
                return new AccessToken(bearerValue);
            }
            return null;
        });
    }
}
