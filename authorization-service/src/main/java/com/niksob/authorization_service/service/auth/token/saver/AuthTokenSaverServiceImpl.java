package com.niksob.authorization_service.service.auth.token.saver;

import com.niksob.authorization_service.service.encoder.auth_token.AuthTokenEncodingService;
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
    private final AuthTokenDatabaseConnector databaseConnector;

    private final AuthTokenEncodingService encodingService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenSaverServiceImpl.class);

    @Override
    public Mono<Void> save(AuthToken authToken) {
        return Mono.just(authToken)
                .map(encodingService::encode)
                .flatMap(databaseConnector::save)
                .doOnSuccess(ignore -> log.info("Successful saving user's auth token", null, authToken));
//                .onErrorResume(throwable -> );
    }
}
