package com.niksob.database_service.service.auth.token.loader;

import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import reactor.core.publisher.Mono;

public interface AuthTokenLoaderService {
    Mono<EncodedAuthToken> load(AuthTokenDetails authTokenDetails);
}
