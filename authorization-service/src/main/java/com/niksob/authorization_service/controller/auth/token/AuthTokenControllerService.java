package com.niksob.authorization_service.controller.auth.token;

import com.niksob.authorization_service.mapper.auth.token.AuthTokenMonoDtoMapper;
import com.niksob.authorization_service.service.auth.token.AuthTokenService;
import com.niksob.domain.mapper.dto.auth.login.RowLoginInDetailsDtoMapper;
import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenService.class,
        mapper = {RowLoginInDetailsDtoMapper.class, AuthTokenDtoMapper.class, AuthTokenMonoDtoMapper.class})
public interface AuthTokenControllerService {
    Mono<AuthTokenDto> generate(RowLoginInDetailsDto loginInDetails);
}
