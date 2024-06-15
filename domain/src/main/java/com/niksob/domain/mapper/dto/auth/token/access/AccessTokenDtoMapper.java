package com.niksob.domain.mapper.dto.auth.token.access;

import com.niksob.domain.dto.auth.token.access.AccessTokenDto;
import com.niksob.domain.model.auth.token.AccessToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccessTokenDtoMapper {
    @Mapping(source = "accessToken", target = "value")
    AccessToken fromDto(AccessTokenDto dto);

    @Mapping(source = "value", target = "accessToken")
    AccessTokenDto toDto(AccessToken refreshToken);
}
