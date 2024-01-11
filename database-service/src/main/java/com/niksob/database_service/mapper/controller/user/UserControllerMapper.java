package com.niksob.database_service.mapper.controller.user;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.mapper.user.UserInfoDtoMapper;
import com.niksob.domain.mapper.user.UsernameDtoMapper;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserControllerMapper {
    private final UsernameDtoMapper usernameDtoMapper;
    private final UserInfoDtoMapper userInfoDtoMapper;

    public Username fromUsernameDto(UsernameDto username) {
        return usernameDtoMapper.fromDto(username);
    }

    public UserInfo fromUserInfoDto(UserInfoDto userInfoDto) {
        return userInfoDtoMapper.fromDto(userInfoDto);
    }

    public Mono<UserInfoDto> toMonoUserInfoDto(UserInfo userInfo) {
        return Mono.just(userInfo)
                .map(userInfoDtoMapper::toDto);
    }

    public Mono<Void> toMonoVoid(UserInfo userInfo) {
        return Mono.empty();
    }
}
