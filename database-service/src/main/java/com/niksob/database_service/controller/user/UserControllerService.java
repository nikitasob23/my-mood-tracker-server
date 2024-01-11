package com.niksob.database_service.controller.user;

import com.niksob.database_service.mapper.controller.user.UserControllerMapper;
import com.niksob.database_service.service.user.UserService;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.mapping_wrapper.annotation.MappingWrapper;
import reactor.core.publisher.Mono;

@MappingWrapper(source = UserService.class, mapper = UserControllerMapper.class)
public interface UserControllerService {
    Mono<UserInfoDto> load(UsernameDto usernameDto);

    Mono<Void> save(UserInfoDto userInfoDto);

    Mono<Void> update(UserInfoDto userInfoDto);

    Mono<Void> delete(UsernameDto usernameDto);
}
