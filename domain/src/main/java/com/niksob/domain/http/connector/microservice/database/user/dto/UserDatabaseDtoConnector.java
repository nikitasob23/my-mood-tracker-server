package com.niksob.domain.http.connector.microservice.database.user.dto;

import com.niksob.domain.dto.user.*;
import reactor.core.publisher.Mono;

public interface UserDatabaseDtoConnector {
    Mono<UserInfoDto> loadById(UserIdDto userId);

    Mono<UserDto> load(UsernameDto usernameDto);

    Mono<FullUserInfoDto> loadFull(UsernameDto usernameDto);

    Mono<Boolean> existsByEmail(EmailDto email);

    Mono<UserInfoDto> save(UserInfoDto userInfoDto);

    Mono<Void> update(UserInfoDto userInfoDto);

    Mono<Void> delete(UsernameDto usernameDto);
}
