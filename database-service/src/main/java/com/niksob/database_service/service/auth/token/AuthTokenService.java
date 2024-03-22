package com.niksob.database_service.service.auth.token;

import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import reactor.core.publisher.Mono;

public interface AuthTokenService {
    Mono<EncodedAuthToken> save(EncodedAuthToken authToken);
}
