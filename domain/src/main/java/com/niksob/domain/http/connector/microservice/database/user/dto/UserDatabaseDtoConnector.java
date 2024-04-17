package com.niksob.domain.http.connector.microservice.database.user.dto;

import com.niksob.domain.dto.user.FullUserInfoDto;
import com.niksob.domain.dto.user.SecurityUserDetailsDto;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import reactor.core.publisher.Mono;

public interface UserDatabaseDtoConnector {
    Mono<SecurityUserDetailsDto> load(UsernameDto usernameDto);

    Mono<FullUserInfoDto> loadFull(UsernameDto usernameDto);

    Mono<UserInfoDto> save(UserInfoDto userInfoDto);

    Mono<Void> update(UserInfoDto userInfoDto);

    Mono<Void> delete(UsernameDto usernameDto);
}
