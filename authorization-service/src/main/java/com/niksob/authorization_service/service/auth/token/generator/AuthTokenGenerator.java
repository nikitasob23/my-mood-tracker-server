package com.niksob.authorization_service.service.auth.token.generator;

import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.user.UserInfo;
import reactor.core.publisher.Mono;

public interface AuthTokenGenerator {
    Mono<AuthToken> generate(UserInfo userInfo);
}
