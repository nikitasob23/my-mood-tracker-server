package com.niksob.domain.mapper.dto.auth.login;

import com.niksob.domain.model.auth.login.SignupDetails;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SignupDetailsDtoMapper {
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "rowPassword", target = "rowPassword.value")
    SignupDetails fromDto(SignupDetailsDto dto);

    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "rowPassword.value", target = "rowPassword")
    SignupDetailsDto toDto(SignupDetails signupDetails);
}
