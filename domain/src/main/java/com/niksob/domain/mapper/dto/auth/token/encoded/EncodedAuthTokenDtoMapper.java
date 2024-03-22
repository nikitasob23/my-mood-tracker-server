package com.niksob.domain.mapper.dto.auth.token.encoded;

import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenIdDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserIdDtoMapper.class, AuthTokenIdDtoMapper.class})
public interface EncodedAuthTokenDtoMapper {
    @Mapping(source = "access.value", target = "access")
    @Mapping(source = "refresh.value", target = "refresh")
    EncodedAuthTokenDto toDto(EncodedAuthToken authToken);

    @Mapping(source = "access", target = "access.value")
    @Mapping(source = "refresh", target = "refresh.value")
    EncodedAuthToken fromDto(EncodedAuthTokenDto dto);
}
