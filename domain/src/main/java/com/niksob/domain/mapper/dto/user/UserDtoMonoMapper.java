package com.niksob.domain.mapper.dto.user;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.model.user.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserDtoMonoMapper {
    private final UserInfoDtoMapper userInfoDtoMapper;

    public Mono<UserInfoDto> toDtoMono(Mono<UserInfo> mono) {
        return mono.map(userInfoDtoMapper::toDto);
    }

    public Mono<Void> toMonoVoid(Mono<UserInfo> mono) {
        return mono.then();
    }
}
