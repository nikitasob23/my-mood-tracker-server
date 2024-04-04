package com.niksob.gateway_service.security.web.auth.converter;

import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.gateway_service.exception.security.account.LockedAccountException;
import com.niksob.gateway_service.exception.security.token.bearer.BearerAuthenticationException;
import com.niksob.gateway_service.exception.token.access.request.AccessTokenNotFoundException;
import com.niksob.gateway_service.security.web.auth.JwtAuthentication;
import com.niksob.gateway_service.service.auth.token.AuthTokenService;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
class AccessTokenAuthenticationConverter implements ServerAuthenticationConverter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";

    private final AuthTokenService authTokenService;
    private final ReactiveUserDetailsService userDetailsService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AccessTokenAuthenticationConverter.class);

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        final String path = exchange.getRequest().getPath().pathWithinApplication().value();

        return extractTokenOrThrow(exchange)
                .flatMap(accessToken -> validateAccessTokenOrThrow(accessToken, path))
                .flatMap(authTokenService::extractAuthDetails)
                .flatMap(authDetails -> userDetailsService.findByUsername(authDetails.getUsername().getValue()))
                .filter(UserDetails::isAccountNonLocked).switchIfEmpty(createLockedAccountExceptionMono())
                .map(userDetails -> (Authentication) new JwtAuthentication(userDetails))
                .onErrorResume(this::createAuthenticationExceptionMono);
    }

    private Mono<AccessToken> validateAccessTokenOrThrow(AccessToken accessToken, String path) {
        return authTokenService.validateAccessToken(accessToken)
                .flatMap(valid -> valid ? Mono.just(accessToken) : Mono.error(createNonValidHttpException(path)));
    }

    private HttpClientException createNonValidHttpException(String path) {
        return new HttpClientException("Not valid token", HttpStatus.FORBIDDEN, path);
    }

    private <T> Mono<T> createAuthenticationExceptionMono(Throwable throwable) {
        final String message = "Failure authentication by bearer";
        var e = new BearerAuthenticationException(message, throwable);
        log.error(message, e);
        return Mono.error(e);
    }

    private <T> Mono<T> createLockedAccountExceptionMono() {
        final String message = "Account is locked";
        log.error(message);
        return Mono.error(new LockedAccountException(message));
    }

    private Mono<AccessToken> extractTokenOrThrow(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> {
            final String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
            if (authHeader != null && authHeader.startsWith(BEARER)) {
                final String bearerValue = authHeader.substring(7);
                return new AccessToken(bearerValue);
            }
            throw new AccessTokenNotFoundException("Bearer not found");
        });
    }
}
