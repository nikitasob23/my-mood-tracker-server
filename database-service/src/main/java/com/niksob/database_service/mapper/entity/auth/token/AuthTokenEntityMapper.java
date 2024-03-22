package com.niksob.database_service.mapper.entity.auth.token;

import com.niksob.database_service.entity.user.token.refresh.AuthTokenEntity;
import com.niksob.database_service.mapper.entity.user.id.UserIdEntityMapper;
import com.niksob.domain.model.auth.token.AuthToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserIdEntityMapper.class)
public interface AuthTokenEntityMapper {
    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "userId.value", target = "userId")
    @Mapping(source = "access.value", target = "encodedAccess")
    @Mapping(source = "refresh.value", target = "encodedRefresh")
    AuthTokenEntity toEntity(AuthToken authToken);

    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "userId", target = "userId.value")
    @Mapping(source = "encodedAccess", target = "access.value")
    @Mapping(source = "encodedRefresh", target = "refresh.value")
    AuthToken fromEntity(AuthTokenEntity authToken);
}
