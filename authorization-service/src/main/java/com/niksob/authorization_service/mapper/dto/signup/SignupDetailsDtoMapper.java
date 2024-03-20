package com.niksob.authorization_service.mapper.dto.signup;

import com.niksob.authorization_service.model.signup.SignupDetails;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SignupDetailsDtoMapper {
    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "rowPassword", target = "rowPassword.value")
    SignupDetails fromDto(SignupDetailsDto dto);
}
