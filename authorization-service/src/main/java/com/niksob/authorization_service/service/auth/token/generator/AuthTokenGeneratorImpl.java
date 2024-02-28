package com.niksob.authorization_service.service.auth.token.generator;

import com.niksob.authorization_service.mapper.auth.token.AuthTokenMapper;
import com.niksob.authorization_service.mapper.jwt_params.claims.JwtDetailsMapper;
import com.niksob.authorization_service.model.jwt.Jwt;
import com.niksob.authorization_service.model.jwt.JwtDetails;
import com.niksob.authorization_service.service.jwt.JwtService;
import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.user.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthTokenGeneratorImpl implements AuthTokenGenerator {
    @Qualifier("accessJwtService")
    private final JwtService accessJwtTokenService;
    @Qualifier("refreshJwtService")
    private final JwtService refreshJwtTokenService;

    private final JwtDetailsMapper jwtDetailsMapper;
    private final AuthTokenMapper authTokenMapper;

    @Override
    public AuthToken generate(UserInfo userInfo) {
        final JwtDetails jwtDetails = jwtDetailsMapper.fromUserInfo(userInfo);
        final Jwt accessJwt = accessJwtTokenService.generate(jwtDetails);
        final AccessToken accessToken = authTokenMapper.toAccessToken(accessJwt);

        final Jwt refreshJwt = refreshJwtTokenService.generate(jwtDetails);
        final RefreshToken refreshToken = authTokenMapper.toRefreshToken(refreshJwt);
        return new AuthToken(accessToken, refreshToken);
    }
}
