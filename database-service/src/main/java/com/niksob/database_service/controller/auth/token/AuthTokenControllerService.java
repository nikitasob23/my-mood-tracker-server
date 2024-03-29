package com.niksob.database_service.controller.auth.token;

import com.niksob.database_service.service.auth.token.loader.AuthTokenLoaderService;
import com.niksob.database_service.service.auth.token.updater.AuthTokenUpdaterService;
import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDetailsDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.encoded.EncodedAuthTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.encoded.mono.EncodedAuthTokenMonoDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenUpdaterService.class, sourceParents = AuthTokenLoaderService.class, mapper = {
        EncodedAuthTokenDtoMapper.class,
        EncodedAuthTokenMonoDtoMapper.class,
        AuthTokenDetailsDtoMapper.class,
        UserIdDtoMapper.class
})
public interface AuthTokenControllerService {
    Mono<Boolean> existsByDetails(AuthTokenDetailsDto authTokenDetails);

    Mono<EncodedAuthTokenDto> load(AuthTokenDetailsDto authTokenDetails);

    Mono<EncodedAuthTokenDto> save(EncodedAuthTokenDto authTokenDto);

    Mono<EncodedAuthTokenDto> update(EncodedAuthTokenDto authTokenDto);

    Mono<Void> delete(AuthTokenDetailsDto authTokenDetails);

    Mono<Void> deleteByUserId(UserIdDto userId);
}
