package com.niksob.authorization_service.controller.auth.token;

import com.niksob.authorization_service.service.auth.token.AuthTokenService;
import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.dto.auth.token.access.AccessTokenDto;
import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.dto.auth.token.encoded.refresh.RefreshTokenDto;
import com.niksob.domain.mapper.dto.auth.login.RowLoginInDetailsDtoMapper;
import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.access.AccessTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.details.mono.AuthTokenDetailsDtoMonoMapper;
import com.niksob.domain.mapper.dto.auth.token.mono.AuthTokenMonoDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.refresh.RefreshTokenDtoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenService.class, mapper = {
        RowLoginInDetailsDtoMapper.class,
        AuthTokenDtoMapper.class,
        AuthTokenMonoDtoMapper.class,
        RefreshTokenDtoMapper.class,
        AccessTokenDtoMapper.class,
        UserInfoDtoMapper.class,
        AuthTokenDetailsDtoMonoMapper.class
})
public interface AuthTokenControllerService {
    Mono<AuthTokenDto> generate(RowLoginInDetailsDto loginInDetails);

    Mono<AuthTokenDto> generateByRefresh(RefreshTokenDto refreshToken);

    Mono<Boolean> validateAccessToken(AccessTokenDto accessToken);

    Mono<AuthTokenDetailsDto> extractAuthDetails(AccessTokenDto accessToken);
}
