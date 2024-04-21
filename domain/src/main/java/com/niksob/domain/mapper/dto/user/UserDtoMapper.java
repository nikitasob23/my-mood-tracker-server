package com.niksob.domain.mapper.dto.user;

import com.niksob.domain.dto.user.UserDto;
import com.niksob.domain.model.user.User;
import com.niksob.domain.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "password.value", target = "password")
    UserDto toDto(UserInfo userInfo);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "email", target = "email.value")
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "password", target = "password.value")
    @Mapping(target = "moodEntries", ignore = true)
    @Mapping(target = "moodTags", ignore = true)
    UserInfo fromDto(UserDto dto);

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "password.value", target = "password")
    UserDto toSecurityUserDetailsDto(User user);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "email", target = "email.value")
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "password", target = "password.value")
    User toSecurityUserDetails(UserDto dto);

    UserInfo toUserInfo(User userDetails);

    default Mono<UserDto> toMonoDto(Mono<UserInfo> mono) {
        return mono.map(this::toDto);
    }

    default Mono<UserInfo> fromMonoDto(Mono<UserDto> mono) {
        return mono.map(this::fromDto);
    }

    default Mono<UserDto> toSecurityUserDetailsMonoDto(Mono<User> mono) {
        return mono.map(this::toSecurityUserDetailsDto);
    }

    default Mono<User> toSecurityUserDetailsMono(Mono<UserDto> mono) {
        return mono.map(this::toSecurityUserDetails);
    }
}
