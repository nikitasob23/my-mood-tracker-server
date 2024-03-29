package com.niksob.domain.mapper.dto.auth.token.refresh;

import com.niksob.domain.dto.auth.token.encoded.refresh.RefreshTokenDto;
import com.niksob.domain.model.auth.token.RefreshToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefreshTokenDtoMapper {
    @Mapping(source = "refreshToken", target = "value")
    RefreshToken fromDto(RefreshTokenDto dto);
}
