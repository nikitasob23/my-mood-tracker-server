package com.niksob.domain.http.connector.auth.token.dto;

import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import reactor.core.publisher.Mono;

public interface AuthTokenDatabaseDtoConnector {
    Mono<Boolean> existsByDetails(AuthTokenDetailsDto authTokenDetails);

    Mono<EncodedAuthTokenDto> save(EncodedAuthTokenDto authTokenDto);

    Mono<Void> update(EncodedAuthTokenDto authToken);
}
