package com.niksob.domain.http.connector.microservice.auth.token;

import com.niksob.domain.http.connector.microservice.auth.token.dto.AuthTokenDtoConnector;
import com.niksob.domain.mapper.dto.auth.login.RowLoginInDetailsDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.mono.AuthTokenMonoDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.refresh.RefreshTokenDtoMapper;
import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenDtoConnector.class, mapper = {
        RowLoginInDetailsDtoMapper.class,
        AuthTokenDtoMapper.class,
        AuthTokenMonoDtoMapper.class,
        RefreshTokenDtoMapper.class
})
public interface AuthTokenConnector {
    Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails);

    Mono<AuthToken> generateByRefresh(RefreshToken refreshToken);
}
