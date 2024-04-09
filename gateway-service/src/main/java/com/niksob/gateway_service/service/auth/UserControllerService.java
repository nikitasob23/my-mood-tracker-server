package com.niksob.gateway_service.service.auth;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.mapper.dto.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.UsernameDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = UserService.class,
        mapper = {UsernameDtoMapper.class, UserInfoDtoMapper.class, UserIdDtoMapper.class, UserDtoMonoMapper.class})
public interface UserControllerService {
    Mono<UserInfoDto> loadAllByUsername(UsernameDto username);

    Mono<UserInfoDto> save(UserInfoDto userInfo);

    Mono<Void> update(UserInfoDto userInfo);

    Mono<Void> delete(UsernameDto username);
}
