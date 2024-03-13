package com.niksob.database_service.mapper.entity.user.id;

import com.niksob.domain.model.user.UserId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserIdEntityMapper {
    default UserId fromEntity(Long id) {
        return new UserId(id);
    }

    default Long toEntity(UserId userId) {
        return userId.getValue();
    }
}
