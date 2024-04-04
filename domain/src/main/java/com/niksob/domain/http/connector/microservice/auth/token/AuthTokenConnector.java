package com.niksob.domain.http.connector.microservice.auth.token;

import com.niksob.domain.http.connector.microservice.auth.token.dto.AuthTokenDtoConnector;
import com.niksob.domain.mapper.dto.auth.login.RowLoginInDetailsDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.access.AccessTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.details.mono.AuthTokenDetailsDtoMonoMapper;
import com.niksob.domain.mapper.dto.auth.token.mono.AuthTokenMonoDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.refresh.RefreshTokenDtoMapper;
import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenDtoConnector.class, mapper = {
        RowLoginInDetailsDtoMapper.class,
        AuthTokenDtoMapper.class,
        AuthTokenMonoDtoMapper.class,
        AuthTokenDetailsDtoMonoMapper.class,
        RefreshTokenDtoMapper.class,
        AccessTokenDtoMapper.class
})
public interface AuthTokenConnector {
    Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails);

    Mono<AuthToken> generateByRefresh(RefreshToken refreshToken);

    Mono<Boolean> validateAccessToken(AccessToken accessToken);

    Mono<AuthTokenDetails> extractAuthDetails(AccessToken accessToken);
}
