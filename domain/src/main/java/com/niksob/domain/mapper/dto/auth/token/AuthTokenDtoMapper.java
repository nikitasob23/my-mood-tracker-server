package com.niksob.domain.mapper.dto.auth.token;

import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.domain.model.auth.token.AuthToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserIdDtoMapper.class, AuthTokenIdDtoMapper.class})
public interface AuthTokenDtoMapper {
    @Mapping(source = "access.value", target = "access")
    @Mapping(source = "refresh.value", target = "refresh")
    AuthTokenDto toDto(AuthToken authToken);

    @Mapping(source = "access", target = "access.value")
    @Mapping(source = "refresh", target = "refresh.value")
    AuthToken fromDto(AuthTokenDto dto);
}
