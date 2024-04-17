package com.niksob.database_service.controller.user;

import com.niksob.database_service.service.user.UserService;
import com.niksob.database_service.service.user.loader.UserLoader;
import com.niksob.domain.dto.user.FullUserInfoDto;
import com.niksob.domain.dto.user.SecurityUserDetailsDto;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.mapper.dto.user.SecurityUserDetailsDtoMapper;
import com.niksob.domain.mapper.dto.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.UsernameDtoMapper;
import com.niksob.domain.mapper.dto.user.full.FullUserInfoDtoMapper;
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
                FullUserInfoDtoMapper.class,
                SecurityUserDetailsDtoMapper.class
        }
)
public interface UserControllerService {
    Mono<SecurityUserDetailsDto> loadByUsername(UsernameDto usernameDto);

    Mono<FullUserInfoDto> loadFullByUsername(UsernameDto usernameDto);

    Mono<UserInfoDto> save(UserInfoDto userInfoDto);

    Mono<Void> update(UserInfoDto userInfoDto);

    Mono<Void> delete(UsernameDto usernameDto);
}
