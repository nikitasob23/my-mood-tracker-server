package com.niksob.authorization_service.service.auth.token.generator;

import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import reactor.core.publisher.Mono;

public interface AuthTokenAdapter {
    Mono<AuthToken> generate(AuthTokenDetails authTokenDetails);

    Mono<AuthTokenDetails> extractAuthTokenDetails(RefreshToken refreshToken);
}
