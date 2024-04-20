package com.niksob.domain.mapper.dto.user;

import com.niksob.domain.dto.user.SecurityUserDetailsDto;
import com.niksob.domain.model.user.SecurityUserDetails;
import com.niksob.domain.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import reactor.core.publisher.Mono;

@Mapper(componentModel = "spring")
public interface SecurityUserDetailsDtoMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "password.value", target = "password")
    SecurityUserDetailsDto toDto(UserInfo userInfo);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "email", target = "email.value")
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "password", target = "password.value")
    @Mapping(target = "moodEntries", ignore = true)
    @Mapping(target = "moodTags", ignore = true)
    UserInfo fromDto(SecurityUserDetailsDto dto);

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "password.value", target = "password")
    SecurityUserDetailsDto toSecurityUserDetailsDto(SecurityUserDetails securityUserDetails);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "email", target = "email.value")
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "password", target = "password.value")
    SecurityUserDetails toSecurityUserDetails(SecurityUserDetailsDto dto);

    UserInfo toUserInfo(SecurityUserDetails userDetails);

    default Mono<SecurityUserDetailsDto> toMonoDto(Mono<UserInfo> mono) {
        return mono.map(this::toDto);
    }

    default Mono<UserInfo> fromMonoDto(Mono<SecurityUserDetailsDto> mono) {
        return mono.map(this::fromDto);
    }

    default Mono<SecurityUserDetailsDto> toSecurityUserDetailsMonoDto(Mono<SecurityUserDetails> mono) {
        return mono.map(this::toSecurityUserDetailsDto);
    }

    default Mono<SecurityUserDetails> toSecurityUserDetailsMono(Mono<SecurityUserDetailsDto> mono) {
        return mono.map(this::toSecurityUserDetails);
    }
}
