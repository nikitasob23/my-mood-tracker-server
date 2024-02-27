package com.niksob.domain.http.connector;

import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = UserDatabaseDtoConnector.class, mapper = UserInfoDtoMapper.class)
public interface UserDatabaseConnector {
    Mono<Void> save(UserInfo userInfo);
}
