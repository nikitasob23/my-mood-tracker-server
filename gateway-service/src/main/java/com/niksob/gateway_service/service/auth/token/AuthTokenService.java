package com.niksob.gateway_service.service.auth.token;

import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AuthToken;
import reactor.core.publisher.Mono;

public interface AuthTokenService {
    Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails);
}
