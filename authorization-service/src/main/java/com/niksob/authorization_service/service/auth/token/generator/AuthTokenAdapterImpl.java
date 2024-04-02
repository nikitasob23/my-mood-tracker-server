package com.niksob.authorization_service.service.auth.token.generator;

import com.niksob.authorization_service.exception.auth.token.AuthTokenException;
import com.niksob.authorization_service.exception.auth.token.expired.ExpiredAuthTokenException;
import com.niksob.authorization_service.exception.auth.token.invalid.InvalidAuthTokenException;
import com.niksob.authorization_service.exception.jwt.InvalidJwtException;
import com.niksob.authorization_service.mapper.auth.token.AuthTokenMapper;
import com.niksob.authorization_service.mapper.jwt.JwtMapper;
import com.niksob.authorization_service.mapper.jwt.params.claims.JwtDetailsMapper;
import com.niksob.authorization_service.model.jwt.Jwt;
import com.niksob.authorization_service.model.jwt.JwtDetails;
import com.niksob.authorization_service.service.jwt.JwtService;
import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class AuthTokenAdapterImpl implements AuthTokenAdapter {
    @Qualifier("accessJwtService")
    private final JwtService accessJwtTokenService;
    @Qualifier("refreshJwtService")
    private final JwtService refreshJwtTokenService;

    private final JwtDetailsMapper jwtDetailsMapper;
    private final JwtMapper jwtMapper;
    private final AuthTokenMapper authTokenMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenAdapterImpl.class);

    @Override
    public Mono<AuthToken> generate(AuthTokenDetails authTokenDetails) {
        final Mono<JwtDetails> jwtDetailsMono = Mono.just(authTokenDetails)
                .map(jwtDetailsMapper::toAuthTokenDetails);

        final Mono<AccessToken> accessTokenMono =
                createTokenMono(jwtDetailsMono, accessJwtTokenService, jwtMapper::toAccessToken);
        final Mono<RefreshToken> refreshTokenMono =
                createTokenMono(jwtDetailsMono, refreshJwtTokenService, jwtMapper::toRefreshToken);

        return accessTokenMono.zipWith(refreshTokenMono,
                (access, refresh) -> authTokenMapper.combine(authTokenDetails, access, refresh));
    }

    @Override
    public Mono<AuthTokenDetails> extractAuthTokenDetails(RefreshToken refreshToken) {
        return Mono.fromCallable(() -> {
            final Jwt jwt = jwtMapper.fromRefreshToken(refreshToken);
            final JwtDetails jwtDetails = refreshJwtTokenService.getJwtDetails(jwt);
            return jwtDetailsMapper.toAuthTokenDetails(jwtDetails);
        }).onErrorResume(this::createAuthTokenException);
    }

    private <T> Mono<T> createTokenMono(
            Mono<JwtDetails> jwtDetailsMono, JwtService jwtService, Function<Jwt, T> jwtToTokenMapper
    ) {
        return jwtDetailsMono.map(jwtService::generate)
                .onErrorResume(this::createAuthTokenException)
                .map(jwtToTokenMapper)
                .subscribeOn(Schedulers.parallel());
    }

    private <T> Mono<T> createAuthTokenException(Throwable throwable) {
        Throwable e;
        if (throwable instanceof ExpiredJwtException) {
            final String message = "Token is expired";
            log.error(message);
            e = new ExpiredAuthTokenException(message, throwable);
        } else if (throwable instanceof InvalidJwtException) {
            final String message = "Not valid auth token";
            log.error(message, throwable);
            e = new InvalidAuthTokenException(message, throwable);
        } else {
            log.error(throwable.getMessage());
            e = new AuthTokenException(throwable);
        }
        return Mono.error(e);
    }
}
