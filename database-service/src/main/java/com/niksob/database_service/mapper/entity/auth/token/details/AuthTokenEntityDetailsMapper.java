package com.niksob.database_service.mapper.entity.auth.token.details;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.database_service.model.auth.token.details.AuthTokenEntityDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthTokenEntityDetailsMapper {
    AuthTokenEntityDetails fromAuthTokenEntity(AuthTokenEntity entity);

    @Mapping(source = "userId.value", target = "userId")
    AuthTokenEntityDetails toEntity(AuthTokenDetails authTokenDetails);

    @Mapping(source = "userId", target = "userId.value")
    AuthTokenDetails fromEntity(AuthTokenEntityDetails authTokenDetails);
}
