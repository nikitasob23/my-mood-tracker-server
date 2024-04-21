package com.niksob.gateway_service.service.auth;

import com.niksob.domain.dto.user.FullUserInfoDto;
import com.niksob.domain.dto.user.UserDto;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.mapper.dto.user.UserDtoMapper;
import com.niksob.domain.mapper.dto.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.UsernameDtoMapper;
import com.niksob.domain.mapper.dto.user.full.FullUserInfoDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = UserService.class,
        mapper = {
                UsernameDtoMapper.class,
                UserInfoDtoMapper.class,
                UserIdDtoMapper.class,
                UserDtoMonoMapper.class,
                FullUserInfoDtoMapper.class,
                UserDtoMapper.class
        }
)
public interface UserControllerService {
    Mono<UserDto> loadByUsername(UsernameDto username);

    Mono<FullUserInfoDto> loadFullByUsername(UsernameDto username);

    Mono<Void> update(UserInfoDto userInfo);

    Mono<Void> delete(UsernameDto username);
}
