package com.niksob.database_service.mapper.entity.auth.token;

import com.niksob.database_service.entity.user.token.refresh.AuthTokenEntity;
import com.niksob.database_service.mapper.entity.user.id.UserIdEntityMapper;
import com.niksob.domain.model.auth.token.UserAuthToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserIdEntityMapper.class)
public interface AuthTokenEntityMapper {
    @Mapping(source = "access.value", target = "encodedAccess")
    @Mapping(source = "refresh.value", target = "encodedRefresh")
    AuthTokenEntity toEntity(UserAuthToken authToken);

    @Mapping(source = "encodedAccess", target = "access.value")
    @Mapping(source = "encodedRefresh", target = "refresh.value")
    UserAuthToken toEntity(AuthTokenEntity authToken);
}
