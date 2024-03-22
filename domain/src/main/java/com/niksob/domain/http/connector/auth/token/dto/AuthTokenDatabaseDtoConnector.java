package com.niksob.domain.http.connector.auth.token.dto;

import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import reactor.core.publisher.Mono;

public interface AuthTokenDatabaseDtoConnector {
    Mono<EncodedAuthTokenDto> save(EncodedAuthTokenDto authTokenDto);
}
