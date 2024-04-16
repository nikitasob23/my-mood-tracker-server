package com.niksob.domain.mapper.dto.user;

import com.niksob.domain.dto.user.SecurityUserDetailsDto;
import com.niksob.domain.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface SecurityUserDetailsDtoMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "nickname.value", target = "nickname")
    @Mapping(source = "password.value", target = "password")
    SecurityUserDetailsDto toDto(UserInfo userInfo);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "nickname", target = "nickname.value")
    @Mapping(source = "password", target = "password.value")
    @Mapping(target = "moodEntries", ignore = true)
    @Mapping(target = "moodTags", ignore = true)
    UserInfo fromDto(SecurityUserDetailsDto dto);

    default Mono<SecurityUserDetailsDto> toMonoDto(Mono<UserInfo> mono) {
        return mono.map(this::toDto);
    }

    default Mono<UserInfo> fromMonoDto(Mono<SecurityUserDetailsDto> mono) {
        return mono.map(this::fromDto);
    }
}
