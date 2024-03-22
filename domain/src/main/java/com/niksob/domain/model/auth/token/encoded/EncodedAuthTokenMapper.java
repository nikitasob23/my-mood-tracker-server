package com.niksob.domain.model.auth.token.encoded;

import com.niksob.domain.model.auth.token.AuthToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EncodedAuthTokenMapper {
    @Mapping(source = "authToken.id", target = "id")
    @Mapping(source = "accessToken", target = "access")
    @Mapping(source = "refreshToken", target = "refresh")
    EncodedAuthToken combine(AuthToken authToken, EncodedAccessToken accessToken, EncodedRefreshToken refreshToken);
}
