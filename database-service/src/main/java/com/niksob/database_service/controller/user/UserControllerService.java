package com.niksob.database_service.controller.user;

import com.niksob.database_service.service.user.UserService;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.mapper.dto.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.UsernameDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = UserService.class, mapper = {UsernameDtoMapper.class, UserInfoDtoMapper.class, UserDtoMonoMapper.class})
public interface UserControllerService {
    Mono<UserInfoDto> load(UsernameDto usernameDto);

    Mono<Void> save(UserInfoDto userInfoDto);

    Mono<Void> update(UserInfoDto userInfoDto);

    Mono<Void> delete(UsernameDto usernameDto);
}
