package com.niksob.domain.mapper.dto.auth.token;

import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthTokenDetailsDtoMapper {
    @Mapping(source = "username.value", target = "username")
    @Mapping(source = "userId.value", target = "userId")
    AuthTokenDetailsDto toDto(AuthTokenDetails details);

    @Mapping(source = "username", target = "username.value")
    @Mapping(source = "userId", target = "userId.value")
    AuthTokenDetails fromDto(AuthTokenDetailsDto dto);
}
