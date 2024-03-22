package com.niksob.domain.mapper.dto.user.id;

import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.model.user.UserId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserIdDtoMapper {
    default UserIdDto toDto(UserId userId) {
        return new UserIdDto(userId.getValue().toString());
    }

    default UserId toDto(UserIdDto dto) {
        final Long value = Long.parseLong(dto.getValue());
        return new UserId(value);
    }

    default UserId fromDto(String idValue) {
        final long idLong = Long.parseLong(idValue);
        return new UserId(idLong);
    }
}
