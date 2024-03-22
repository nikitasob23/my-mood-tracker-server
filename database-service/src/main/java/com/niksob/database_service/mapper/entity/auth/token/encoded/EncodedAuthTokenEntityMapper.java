package com.niksob.database_service.mapper.entity.auth.token.encoded;

import com.niksob.database_service.entity.user.token.refresh.AuthTokenEntity;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EncodedAuthTokenEntityMapper {
    @Mapping(source = "id", target = "id.value")
    @Mapping(source = "userId", target = "userId.value")
    @Mapping(source = "encodedAccess", target = "access.value")
    @Mapping(source = "encodedRefresh", target = "refresh.value")
    EncodedAuthToken fromEntity(AuthTokenEntity authToken);

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "userId.value", target = "userId")
    @Mapping(source = "access.value", target = "encodedAccess")
    @Mapping(source = "refresh.value", target = "encodedRefresh")
    AuthTokenEntity toEntity(EncodedAuthToken authToken);
}
