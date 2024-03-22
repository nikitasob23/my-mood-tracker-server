package com.niksob.database_service.controller.auth.token;

import com.niksob.database_service.service.auth.token.AuthTokenService;
import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import com.niksob.domain.mapper.dto.auth.token.encoded.EncodedAuthTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.encoded.mono.EncodedAuthTokenMonoDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenService.class,
        mapper = {EncodedAuthTokenDtoMapper.class, EncodedAuthTokenMonoDtoMapper.class})
public interface AuthTokenControllerService {
    Mono<EncodedAuthTokenDto> save(EncodedAuthTokenDto authTokenDto);
}
