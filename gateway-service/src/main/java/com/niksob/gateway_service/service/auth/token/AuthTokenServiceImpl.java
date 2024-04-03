package com.niksob.gateway_service.service.auth.token;

import com.niksob.domain.http.connector.microservice.auth.token.AuthTokenConnector;
import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final AuthTokenConnector authTokenConnector;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenServiceImpl.class);

    @Override
    public Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails) {
        return authTokenConnector.generate(rowLoginInDetails)
                .doOnSuccess(ignore -> log.info("Successful auth token generation", null, rowLoginInDetails))
                .doOnError(throwable -> log.error("Failure auth token generation", throwable, rowLoginInDetails));
    }

    @Override
    public Mono<AuthToken> generateByRefresh(RefreshToken refreshToken) {
        return authTokenConnector.generateByRefresh(refreshToken)
                .doOnSuccess(ignore ->
                        log.info("Successful auth token generation by refresh token", null, refreshToken)
                ).doOnError(throwable ->
                        log.error("Failure auth token generation  by refresh token", throwable, refreshToken)
                );
    }
}
