package com.niksob.domain.http.connector.auth.token.dto;

import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import com.niksob.domain.dto.user.UserIdDto;
import reactor.core.publisher.Mono;

public interface AuthTokenDatabaseDtoConnector {
    Mono<Boolean> existsByDetails(AuthTokenDetailsDto authTokenDetails);

    Mono<EncodedAuthTokenDto> load(AuthTokenDetailsDto authTokenDetails);

    Mono<EncodedAuthTokenDto> save(EncodedAuthTokenDto authTokenDto);

    Mono<EncodedAuthTokenDto> update(EncodedAuthTokenDto authToken);

    Mono<Void> delete(AuthTokenDetailsDto authTokenDetails);

    Mono<Void> deleteByUserId(UserIdDto userId);
}
