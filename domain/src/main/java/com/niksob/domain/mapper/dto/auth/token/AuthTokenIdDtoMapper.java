package com.niksob.domain.mapper.dto.auth.token;

import com.niksob.domain.model.auth.token.AuthTokenId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthTokenIdDtoMapper {
    default String toDto(AuthTokenId id) {
        if (id == null) {
            return null;
        }
        return id.getValue().toString();
    }

    default AuthTokenId fromDto(String idDto) {
        if (idDto == null) {
            return null;
        }
        final Long value = Long.parseLong(idDto);
        return new AuthTokenId(value);
    }
}
