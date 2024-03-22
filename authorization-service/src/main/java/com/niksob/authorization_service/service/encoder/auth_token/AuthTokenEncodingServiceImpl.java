package com.niksob.authorization_service.service.encoder.auth_token;

import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.encoded.EncodedAccessToken;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.domain.model.auth.token.encoded.EncodedRefreshToken;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthTokenEncodingServiceImpl implements AuthTokenEncodingService {
    private final PasswordEncoder encoder;

    @Override
    public EncodedAuthToken encode(AuthToken authToken) {
        final EncodedAccessToken encodedAccessToken = encode(authToken.getAccess());
        final EncodedRefreshToken encodedRefreshToken = encode(authToken.getRefresh());
        return new EncodedAuthToken(authToken.getId(), authToken.getUserId(), encodedAccessToken, encodedRefreshToken);
    }

    @Override
    public EncodedAccessToken encode(AccessToken accessToken) {
        var tokenValue = encoder.encode(accessToken.getValue());
        return new EncodedAccessToken(tokenValue);
    }

    @Override
    public EncodedRefreshToken encode(RefreshToken refreshToken) {
        var tokenValue = encoder.encode(refreshToken.getValue());
        return new EncodedRefreshToken(tokenValue);
    }

    @Override
    public boolean matches(AuthToken authToken, EncodedAuthToken encodedAuthToken) {
        final boolean accessTokenMatches = matches(authToken.getAccess(), encodedAuthToken.getAccess());
        final boolean refreshTokenMatches = matches(authToken.getRefresh(), encodedAuthToken.getRefresh());
        return accessTokenMatches && refreshTokenMatches;
    }

    @Override
    public boolean matches(AccessToken accessToken, EncodedAccessToken encodedAccessToken) {
        return encoder.matches(accessToken.getValue(), encodedAccessToken.getValue());
    }

    @Override
    public boolean matches(RefreshToken refreshToken, EncodedRefreshToken encodedRefreshToken) {
        return encoder.matches(refreshToken.getValue(), encodedRefreshToken.getValue());
    }
}
