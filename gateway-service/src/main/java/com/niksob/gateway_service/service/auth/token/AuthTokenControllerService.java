package com.niksob.gateway_service.service.auth.token;

import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.dto.auth.token.encoded.refresh.RefreshTokenDto;
import com.niksob.domain.mapper.dto.auth.login.RowLoginInDetailsDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.mono.AuthTokenMonoDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.refresh.RefreshTokenDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenService.class, mapper = {
        RowLoginInDetailsDtoMapper.class,
        AuthTokenDtoMapper.class,
        AuthTokenMonoDtoMapper.class,
        RefreshTokenDtoMapper.class
})
public interface AuthTokenControllerService {
    Mono<AuthTokenDto> generate(RowLoginInDetailsDto rowLoginInDetails);

    Mono<AuthTokenDto> generateByRefresh(RefreshTokenDto refreshToken);
}
