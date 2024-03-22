package com.niksob.authorization_service.service.auth.token.generator;

import com.niksob.authorization_service.mapper.auth.token.JwtMapper;
import com.niksob.authorization_service.mapper.jwt_params.claims.JwtDetailsMapper;
import com.niksob.authorization_service.model.jwt.Jwt;
import com.niksob.authorization_service.model.jwt.JwtDetails;
import com.niksob.authorization_service.service.jwt.JwtService;
import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.domain.model.auth.token.UserAuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.user.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class AuthTokenGeneratorImpl implements AuthTokenGenerator {
    @Qualifier("accessJwtService")
    private final JwtService accessJwtTokenService;
    @Qualifier("refreshJwtService")
    private final JwtService refreshJwtTokenService;

    private final JwtDetailsMapper jwtDetailsMapper;
    private final JwtMapper jwtMapper;

    @Override
    public Mono<UserAuthToken> generate(UserInfo userInfo) {
        final Mono<JwtDetails> jwtDetailsMono = Mono.just(userInfo).map(jwtDetailsMapper::fromUserInfo);

        final Mono<AccessToken> accessTokenMono =
                createTokenMono(jwtDetailsMono, accessJwtTokenService, jwtMapper::toAccessToken);
        final Mono<RefreshToken> refreshTokenMono =
                createTokenMono(jwtDetailsMono, refreshJwtTokenService, jwtMapper::toRefreshToken);

        return accessTokenMono.zipWith(refreshTokenMono,
                (access, refresh) -> new UserAuthToken(userInfo.getId(), access, refresh));
    }

    private <T> Mono<T> createTokenMono(
            Mono<JwtDetails> jwtDetailsMono, JwtService jwtService, Function<Jwt, T> jwtToTokenMapper
    ) {
        return jwtDetailsMono.map(jwtService::generate)
                .map(jwtToTokenMapper)
                .subscribeOn(Schedulers.parallel());
    }
}
