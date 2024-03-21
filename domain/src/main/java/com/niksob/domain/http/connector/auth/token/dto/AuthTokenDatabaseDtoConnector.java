package com.niksob.domain.http.connector.auth.token.dto;

import com.niksob.domain.dto.auth.token.AuthTokenDto;
import reactor.core.publisher.Mono;

public interface AuthTokenDatabaseDtoConnector {
    Mono<Void> save(AuthTokenDto authTokenDto);
}
