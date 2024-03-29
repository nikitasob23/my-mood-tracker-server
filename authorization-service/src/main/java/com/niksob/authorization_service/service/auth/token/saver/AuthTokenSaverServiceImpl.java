package com.niksob.authorization_service.service.auth.token.saver;

import com.niksob.authorization_service.mapper.auth.token.AuthTokenMapper;
import com.niksob.authorization_service.service.encoder.auth_token.AuthTokenEncodingService;
import com.niksob.domain.http.connector.auth.token.AuthTokenDatabaseConnector;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthTokenMapper;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthTokenSaverServiceImpl implements AuthTokenSaverService {
    private final AuthTokenDatabaseConnector databaseConnector;
    private final AuthTokenEncodingService encodingService;

    private final AuthTokenMapper authTokenMapper;
    private final EncodedAuthTokenMapper encodedAuthTokenMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenSaverServiceImpl.class);

    @Override
    public Mono<AuthToken> upsert(AuthToken authToken) {
        return Mono.just(authToken)
                .map(encodingService::encode)
                .flatMap(this::upsertInStorage)
                .map(encodedToken -> authTokenMapper.combine(encodedToken, authToken))
                .doOnNext(token -> log.info("Successful upsert user's auth token", null, token))
                .doOnError(throwable -> log.error("Failure upsert user's auth token", null, authToken));
    }

    @Override
    public Mono<AuthToken> update(AuthToken authToken) {
        return Mono.just(authToken)
                .map(encodingService::encode)
                .flatMap(databaseConnector::update)
                .map(encodedToken -> authTokenMapper.combine(encodedToken, authToken))
                .doOnNext(token -> log.info("Successful update user's auth token", null, token))
                .doOnError(throwable -> log.error("Failure update user's auth token", null, authToken));
    }

    private Mono<EncodedAuthToken> upsertInStorage(EncodedAuthToken authToken) {
        return Mono.just(authToken)
                .map(encodedAuthTokenMapper::getDetails)
                .flatMap(this::existsInStorage)
                .flatMap(ignore -> databaseConnector.update(authToken))
                .switchIfEmpty(databaseConnector.save(authToken));
    }

    private Mono<AuthTokenDetails> existsInStorage(AuthTokenDetails authTokenDetails) {
        return databaseConnector.existsByDetails(authTokenDetails)
                .flatMap(exists -> exists ? Mono.just(authTokenDetails) : Mono.empty());
    }
}
