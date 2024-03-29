package com.niksob.domain.mapper.dto.user.id;

import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.model.user.UserId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserIdDtoMapper {
    default UserIdDto toDto(UserId userId) {
        return new UserIdDto(userId.getValue().toString());
    }

    default String toStringDto(UserId userId) {
        if (userId == null) {
            return null;
        }
        return userId.getValue().toString();
    }

    default UserId fromDto(UserIdDto dto) {
        if (dto == null) {
            return null;
        }
        final Long value = Long.parseLong(dto.getValue());
        return new UserId(value);
    }

    default UserId fromStringDto(String dto) {
        if (dto == null) {
            return null;
        }
        final Long value = Long.parseLong(dto);
        return new UserId(value);
    }
}
