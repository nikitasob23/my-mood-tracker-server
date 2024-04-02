package com.niksob.domain.http.connector.microservice.database.user.dto;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import reactor.core.publisher.Mono;

public interface UserDatabaseDtoConnector {
    Mono<UserInfoDto> load(UsernameDto usernameDto);

    Mono<Void> save(UserInfoDto userInfoDto);
}
