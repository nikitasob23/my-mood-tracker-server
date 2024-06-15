package com.niksob.domain.http.connector.microservice.database.user;

import com.niksob.domain.http.connector.microservice.database.user.dto.UserDatabaseDtoConnector;
import com.niksob.domain.mapper.dto.user.UserDtoMapper;
import com.niksob.domain.mapper.dto.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.UsernameDtoMapper;
import com.niksob.domain.mapper.dto.user.email.EmailDtoMapper;
import com.niksob.domain.mapper.dto.user.full.FullUserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.domain.model.user.*;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = UserDatabaseDtoConnector.class, mapper = {
        UsernameDtoMapper.class,
        EmailDtoMapper.class,
        UserInfoDtoMapper.class,
        UserDtoMonoMapper.class,
        FullUserInfoDtoMapper.class,
        UserDtoMapper.class,
        UserIdDtoMapper.class
})
public interface UserDatabaseConnector {
    Mono<UserInfo> loadById(UserId id);

    Mono<User> load(Username username);

    Mono<UserInfo> loadFull(Username username);

    Mono<Boolean> existsByEmail(Email email);

    Mono<UserInfo> save(UserInfo userInfo);

    Mono<Void> update(UserInfo userInfo);

    Mono<Void> delete(Username username);
}
