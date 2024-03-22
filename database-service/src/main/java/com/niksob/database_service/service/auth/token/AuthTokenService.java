package com.niksob.database_service.service.auth.token;

import com.niksob.domain.model.auth.token.UserAuthToken;
import reactor.core.publisher.Mono;

public interface AuthTokenService {
    Mono<UserAuthToken> save(UserAuthToken authToken);
}
