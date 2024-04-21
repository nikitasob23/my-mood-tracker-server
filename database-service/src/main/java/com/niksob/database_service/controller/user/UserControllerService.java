package com.niksob.database_service.controller.user;

import com.niksob.database_service.service.user.UserServiceImpl;
import com.niksob.database_service.service.user.existence.UserExistenceService;
import com.niksob.database_service.service.user.loader.UserLoader;
import com.niksob.domain.dto.user.*;
import com.niksob.domain.mapper.dto.user.UserDtoMapper;
import com.niksob.domain.mapper.dto.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.UsernameDtoMapper;
import com.niksob.domain.mapper.dto.user.email.EmailDtoMapper;
import com.niksob.domain.mapper.dto.user.full.FullUserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(
        source = UserServiceImpl.class,
        sourceParents = {UserLoader.class, UserExistenceService.class},
        mapper = {
                UsernameDtoMapper.class,
                EmailDtoMapper.class,
                UserInfoDtoMapper.class,
                UserIdDtoMapper.class,
                UserDtoMonoMapper.class,
                FullUserInfoDtoMapper.class,
                UserDtoMapper.class
        }
)
public interface UserControllerService {
    Mono<Boolean> existsByEmail(EmailDto email);

    Mono<UserDto> loadByUsername(UsernameDto usernameDto);

    Mono<FullUserInfoDto> loadFullByUsername(UsernameDto usernameDto);

    Mono<UserInfoDto> save(UserInfoDto userInfoDto);

    Mono<Void> update(UserInfoDto userInfoDto);

    Mono<Void> delete(UsernameDto usernameDto);
}
