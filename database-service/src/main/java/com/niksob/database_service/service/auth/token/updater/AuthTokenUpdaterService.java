package com.niksob.database_service.service.auth.token.updater;

import com.niksob.database_service.service.auth.token.loader.AuthTokenLoaderService;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import reactor.core.publisher.Mono;

public interface AuthTokenUpdaterService extends AuthTokenLoaderService {
    Mono<EncodedAuthToken> save(EncodedAuthToken authToken);

    Mono<EncodedAuthToken> update(EncodedAuthToken authToken);

    Mono<Void> delete(AuthTokenDetails authTokenDetails);
}
