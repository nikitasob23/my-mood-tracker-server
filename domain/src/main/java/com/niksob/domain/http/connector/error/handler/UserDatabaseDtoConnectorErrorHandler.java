package com.niksob.domain.http.connector.error.handler;

import com.niksob.domain.dto.user.UserInfoDto;
import reactor.core.publisher.Mono;

public interface UserDatabaseDtoConnectorErrorHandler {
    Mono<UserInfoDto> createLoadingError(Throwable throwable, Object state);

    Mono<Void> createSavingError(Throwable throwable, Object state);
}
