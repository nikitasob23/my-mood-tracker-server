package com.niksob.domain.mapper.dto.auth.token.encoded;

import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EncodedAuthTokenDtoMapper {
    @Mapping(source = "access.value", target = "accessToken")
    @Mapping(source = "refresh.value", target = "refreshToken")
    AuthTokenDto toDto(EncodedAuthToken authToken);

    @Mapping(source = "accessToken", target = "access.value")
    @Mapping(source = "refreshToken", target = "refresh.value")
    EncodedAuthToken fromDto(AuthTokenDto dto);
}
