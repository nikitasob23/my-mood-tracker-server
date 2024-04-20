package com.niksob.domain.http.connector.microservice.database.user;

import com.niksob.domain.http.connector.microservice.database.user.dto.UserDatabaseDtoConnector;
import com.niksob.domain.mapper.dto.user.SecurityUserDetailsDtoMapper;
import com.niksob.domain.mapper.dto.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.UsernameDtoMapper;
import com.niksob.domain.mapper.dto.user.full.FullUserInfoDtoMapper;
import com.niksob.domain.model.user.SecurityUserDetails;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = UserDatabaseDtoConnector.class, mapper = {
        UsernameDtoMapper.class,
        UserInfoDtoMapper.class,
        UserDtoMonoMapper.class,
        FullUserInfoDtoMapper.class,
        SecurityUserDetailsDtoMapper.class
})
public interface UserDatabaseConnector {
    Mono<SecurityUserDetails> load(Username username);

    Mono<UserInfo> loadFull(Username username);

    Mono<UserInfo> save(UserInfo userInfo);

    Mono<Void> update(UserInfo userInfo);

    Mono<Void> delete(Username username);
}
