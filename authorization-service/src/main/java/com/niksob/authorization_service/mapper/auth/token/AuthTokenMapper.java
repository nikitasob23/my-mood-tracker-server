package com.niksob.authorization_service.mapper.auth.token;

import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.domain.model.user.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthTokenMapper {
    @Mapping(source = "encodedToken.id.value", target = "id.value")
    @Mapping(source = "token.userId.value", target = "userId.value")
    @Mapping(source = "token.access.value", target = "access.value")
    @Mapping(source = "token.refresh.value", target = "refresh.value")
    @Mapping(source = "token.device", target = "device")
    AuthToken combine(EncodedAuthToken encodedToken, AuthToken token);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userInfo.id", target = "userId")
    @Mapping(source = "accessToken", target = "access")
    @Mapping(source = "refreshToken", target = "refresh")
    AuthToken combine(UserInfo userInfo, AccessToken accessToken, RefreshToken refreshToken);
}
