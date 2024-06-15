package com.niksob.domain.mapper.dto.auth.login;

import com.niksob.domain.dto.auth.login.UserEmailDto;
import com.niksob.domain.model.auth.login.UserEmail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEmailDtoMapper {
    @Mapping(source = "userId.value", target = "userId")
    @Mapping(source = "email.value", target = "email")
    UserEmailDto toDto(UserEmail userEmail);

    @Mapping(source = "userId", target = "userId.value")
    @Mapping(source = "email", target = "email.value")
    UserEmail fromDto(UserEmailDto dto);
}
