package com.niksob.authorization_service.service.auth.token;

import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import reactor.core.publisher.Mono;

public interface AuthTokenService {
    Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails);

    Mono<AuthToken> generateByRefresh(RefreshToken refreshToken);

    Mono<Void> invalidate(AuthTokenDetails authTokenDetails);
}
