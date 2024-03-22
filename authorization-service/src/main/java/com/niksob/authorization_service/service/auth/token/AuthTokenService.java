package com.niksob.authorization_service.service.auth.token;

import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.UserAuthToken;
import reactor.core.publisher.Mono;

public interface AuthTokenService {
    Mono<UserAuthToken> generate(RowLoginInDetails rowLoginInDetails);
}
