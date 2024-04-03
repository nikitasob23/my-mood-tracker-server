package com.niksob.domain.http.connector.microservice.auth.token.dto;

import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import com.niksob.domain.dto.auth.token.AuthTokenDto;
import reactor.core.publisher.Mono;

public interface AuthTokenDtoConnector {
    Mono<AuthTokenDto> generate(RowLoginInDetailsDto rowLoginInDetails);
}
