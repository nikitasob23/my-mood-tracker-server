package com.niksob.authorization_service.service.auth.token.saver;

import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import reactor.core.publisher.Mono;

public interface AuthTokenRepoService {
    Mono<Boolean> existsByDetails(AuthTokenDetails authTokenDetails);

    Mono<AuthTokenDetails> filterExists(AuthTokenDetails authTokenDetails);

    Mono<AuthToken> upsert(AuthToken authToken);

    Mono<AuthToken> update(AuthToken authToken);

    Mono<Void> delete(AuthTokenDetails authTokenDetails);
}
