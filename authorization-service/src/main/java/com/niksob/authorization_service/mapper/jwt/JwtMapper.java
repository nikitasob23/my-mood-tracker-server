package com.niksob.authorization_service.mapper.jwt;

import com.niksob.authorization_service.model.jwt.Jwt;
import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JwtMapper {
    AccessToken toAccessToken(Jwt jwt);

    RefreshToken toRefreshToken(Jwt jwt);

    Jwt fromAccessToken(AccessToken accessToken);

    Jwt fromRefreshToken(RefreshToken refreshToken);
}
