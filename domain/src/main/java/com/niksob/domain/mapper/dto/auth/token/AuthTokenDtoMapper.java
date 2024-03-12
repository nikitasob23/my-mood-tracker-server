package com.niksob.domain.mapper.dto.auth.token;

import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.model.auth.token.AuthToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthTokenDtoMapper {
    @Mapping(source = "access.value", target = "accessToken")
    @Mapping(source = "refresh.value", target = "refreshToken")
    AuthTokenDto toDto(AuthToken authToken);

    @Mapping(source = "accessToken", target = "access.value")
    @Mapping(source = "refreshToken", target = "refresh.value")
    AuthToken fromDto(AuthTokenDto dto);
}
