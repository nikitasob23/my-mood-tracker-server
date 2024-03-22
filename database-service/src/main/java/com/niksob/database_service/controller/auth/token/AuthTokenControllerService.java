package com.niksob.database_service.controller.auth.token;

import com.niksob.database_service.service.auth.token.AuthTokenService;
import com.niksob.domain.dto.auth.token.UserAuthTokenDto;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.mono.AuthTokenMonoDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenService.class, mapper = {AuthTokenDtoMapper.class, AuthTokenMonoDtoMapper.class})
public interface AuthTokenControllerService {
    Mono<UserAuthTokenDto> save(UserAuthTokenDto authTokenDto);
}
