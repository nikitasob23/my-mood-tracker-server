package com.niksob.authorization_service.service.auth.token.saver;

import com.niksob.domain.model.auth.token.UserAuthToken;
import reactor.core.publisher.Mono;

public interface AuthTokenSaverService {
    Mono<Void> save(UserAuthToken authToken);
}
