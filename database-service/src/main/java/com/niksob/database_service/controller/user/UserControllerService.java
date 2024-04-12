package com.niksob.database_service.controller.user;

import com.niksob.database_service.service.user.UserService;
import com.niksob.database_service.service.user.loader.UserLoader;
import com.niksob.domain.dto.user.AllUserInfoDto;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.mapper.dto.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.UsernameDtoMapper;
import com.niksob.domain.mapper.dto.user.all.AllUserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(
        source = UserService.class,
        sourceParents = UserLoader.class,
        mapper = {
                UsernameDtoMapper.class,
                UserInfoDtoMapper.class,
                UserIdDtoMapper.class,
                UserDtoMonoMapper.class,
                AllUserInfoDtoMapper.class
        }
)
public interface UserControllerService {
    Mono<AllUserInfoDto> loadAllByUsername(UsernameDto usernameDto);

    Mono<UserInfoDto> save(UserInfoDto userInfoDto);

    Mono<Void> update(UserInfoDto userInfoDto);

    Mono<Void> delete(UsernameDto usernameDto);
}
