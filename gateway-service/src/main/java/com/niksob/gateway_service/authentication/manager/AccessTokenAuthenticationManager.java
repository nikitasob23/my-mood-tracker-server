package com.niksob.gateway_service.authentication.manager;

import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.gateway_service.model.token.authentication.AuthenticationToken;
import com.niksob.gateway_service.security.web.auth.AccessTokenAuthentication;
import com.niksob.gateway_service.service.auth.token.AuthTokenService;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AccessTokenAuthenticationManager implements ReactiveAuthenticationManager {
    private final AuthTokenService authTokenService;
    private final ReactiveUserDetailsService userDetailsService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AccessTokenAuthenticationManager.class);

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        var token = (AuthenticationToken) authentication.getCredentials();
        return validateAccessTokenOrThrow(token)
                .flatMap(authTokenService::extractAuthDetails)
                .flatMap(authTokenDetails ->
                        userDetailsService.findByUsername(authTokenDetails.getUsername().getValue())
                ).flatMap(details ->
                        details.isAccountNonLocked() ? Mono.just(details) : createLockedAccountExceptionMono(token)
                ).map(userDetails -> authenticate(token, userDetails))
                .doOnNext(auth -> log.info("Success access token authentication", authentication.getCredentials()))
                .onErrorResume(throwable -> createAuthenticationMonoError(throwable, token));
    }

    private Authentication authenticate(AuthenticationToken token, UserDetails userDetails) {
        var authentication = new AccessTokenAuthentication(token.getAccessToken(), userDetails);
        authentication.authenticate();
        return authentication;
    }

    private Mono<AccessToken> validateAccessTokenOrThrow(AuthenticationToken token) {
        return authTokenService.validateAccessToken(token.getAccessToken())
                .flatMap(valid -> valid ? Mono.just(token.getAccessToken()) : createInvalidTokenMonoError(token));
    }

    private <T> Mono<T> createAuthenticationMonoError(
            Throwable throwable, AuthenticationToken authenticationToken
    ) {
        final String message = "Failure authentication by access token";
        var e = new HttpClientException(message, HttpStatus.FORBIDDEN, authenticationToken.getPath());
        log.error(message, throwable, authenticationToken);
        return Mono.error(e);
    }

    private <T> Mono<T> createInvalidTokenMonoError(AuthenticationToken token) {
        final String message = "Failed access token validation";
        final Throwable e = new HttpClientException(message, HttpStatus.FORBIDDEN, token.getPath());
        log.error(message, e, token);
        return Mono.error(e);
    }

    private <T> Mono<T> createLockedAccountExceptionMono(AuthenticationToken token) {
        final String message = "Account is locked";
        var e = new HttpClientException(message, HttpStatus.FORBIDDEN, token.getPath());
        log.error(message, e, token);
        return Mono.error(e);
    }
}
