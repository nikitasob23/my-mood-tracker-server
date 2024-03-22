package com.niksob.authorization_service.service.auth.token.saver;

import com.niksob.authorization_service.exception.auth.token.saving.AuthTokenSavingException;
import com.niksob.authorization_service.mapper.auth.token.AuthTokenMapper;
import com.niksob.authorization_service.service.encoder.auth_token.AuthTokenEncodingService;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.http.connector.auth.token.AuthTokenDatabaseConnector;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthTokenSaverServiceImpl implements AuthTokenSaverService {
    public static final String FAILURE_SAVING_USER_AUTH_TOKEN_MESSAGE = "Failure saving user's auth token";

    private final AuthTokenDatabaseConnector databaseConnector;
    private final AuthTokenEncodingService encodingService;
    private final AuthTokenMapper authTokenMapper;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenSaverServiceImpl.class);

    @Override
    public Mono<AuthToken> save(AuthToken authToken) {
        return Mono.just(authToken)
                .map(encodingService::encode)
                .flatMap(databaseConnector::save)
                .map(encodedToken -> authTokenMapper.combine(encodedToken, authToken))
                .doOnSuccess(ignore -> log.info("Successful saving user's auth token", null, authToken))
                .onErrorResume(throwable -> createSavingError(throwable, authToken));
    }

    private <T> Mono<T> createSavingError(Throwable throwable, Object state) {
        Throwable e;
        if (throwable instanceof ResourceSavingException) {
            e = new AuthTokenSavingException(FAILURE_SAVING_USER_AUTH_TOKEN_MESSAGE, throwable);
        } else {
            e = throwable;
        }
        log.error(FAILURE_SAVING_USER_AUTH_TOKEN_MESSAGE, throwable, state);
        return Mono.error(e);
    }
}
