package com.niksob.authorization_service.service.encoder.auth_token;

import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.domain.model.auth.token.UserAuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.encoded.EncodedAccessToken;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.domain.model.auth.token.encoded.EncodedRefreshToken;

public interface AuthTokenEncodingService {
    EncodedAuthToken encode(UserAuthToken authToken);

    EncodedAccessToken encode(AccessToken accessToken);

    EncodedRefreshToken encode(RefreshToken refreshToken);

    boolean matches(UserAuthToken authToken, EncodedAuthToken encodedAuthToken);

    boolean matches(AccessToken accessToken, EncodedAccessToken encodedAccessToken);

    boolean matches(RefreshToken refreshToken, EncodedRefreshToken encodedRefreshToken);
}
