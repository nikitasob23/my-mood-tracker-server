package com.niksob.domain.model.auth.token.encoded;

import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EncodedAuthTokenMapper {
    @Mapping(source = "authToken.id", target = "id")
    @Mapping(source = "accessToken", target = "access")
    @Mapping(source = "refreshToken", target = "refresh")
    EncodedAuthToken combine(AuthToken authToken, EncodedAccessToken accessToken, EncodedRefreshToken refreshToken);

    @Mapping(source = "authTokenWithId.id", target = "id")
    @Mapping(source = "authToken.userId", target = "userId")
    @Mapping(source = "authToken.access", target = "access")
    @Mapping(source = "authToken.refresh", target = "refresh")
    @Mapping(source = "authToken.device", target = "device")
    EncodedAuthToken combine(EncodedAuthToken authTokenWithId, EncodedAuthToken authToken);

    AuthTokenDetails getDetails(EncodedAuthToken entity);
}
