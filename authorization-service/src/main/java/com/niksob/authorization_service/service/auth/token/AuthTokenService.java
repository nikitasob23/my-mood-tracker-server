package com.niksob.authorization_service.service.auth.token;

import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AccessToken;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.user.UserId;
import reactor.core.publisher.Mono;

public interface AuthTokenService {
    Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails);

    Mono<AuthToken> generateByRefresh(RefreshToken refreshToken);

    Mono<Boolean> validateAccessToken(AccessToken accessToken);

    Mono<AuthTokenDetails> extractAuthDetails(AccessToken accessToken);

    Mono<Void> invalidate(AuthTokenDetails authTokenDetails);

    Mono<Void> invalidateByUserId(UserId userId);
}
