package com.niksob.database_service.controller.user;

import com.niksob.database_service.service.user.UserService;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.mapper.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.user.UsernameDtoMapper;
import com.niksob.mapping_wrapper.annotation.MappingWrapper;
import reactor.core.publisher.Mono;

@MappingWrapper(source = UserService.class, mapper = {UsernameDtoMapper.class, UserInfoDtoMapper.class, UserDtoMonoMapper.class})
public interface UserControllerService {
    Mono<UserInfoDto> load(UsernameDto usernameDto);

    Mono<Void> save(UserInfoDto userInfoDto);

    Mono<Void> update(UserInfoDto userInfoDto);

    Mono<Void> delete(UsernameDto usernameDto);
}
