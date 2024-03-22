package com.niksob.authorization_service.service.auth.token.saver;

import com.niksob.domain.model.auth.token.AuthToken;
import reactor.core.publisher.Mono;

public interface AuthTokenSaverService {
    Mono<AuthToken> save(AuthToken authToken);
}
