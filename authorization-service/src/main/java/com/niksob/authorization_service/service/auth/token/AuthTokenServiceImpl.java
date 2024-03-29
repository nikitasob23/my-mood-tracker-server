package com.niksob.authorization_service.service.auth.token;

import com.niksob.authorization_service.service.auth.login.LoginInService;
import com.niksob.authorization_service.service.auth.token.generator.AuthTokenAdapter;
import com.niksob.authorization_service.service.auth.token.saver.AuthTokenSaverService;
import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final LoginInService loginInService;

    private final AuthTokenAdapter authTokenAdapter;
    private final AuthTokenSaverService authTokenSaverService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenServiceImpl.class);

    @Override
    public Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails) {
        return loginInService.loginInOrThrow(rowLoginInDetails)
                .map(userId -> new AuthTokenDetails(userId, rowLoginInDetails.getDevice()))
                .flatMap(authTokenAdapter::generate)
                .flatMap(authTokenSaverService::upsert)
                .doOnNext(this::logSuccessGeneration)
                .doOnError(throwable -> logFailureGeneration(throwable, rowLoginInDetails));
    }

    @Override
    public Mono<AuthToken> generateByRefresh(RefreshToken refreshToken) {
        return authTokenAdapter.extractAuthTokenDetails(refreshToken) // if expired then throw exception
                .flatMap(authTokenAdapter::generate)
                .flatMap(authTokenSaverService::update)
                .doOnNext(this::logSuccessGeneration)
                .doOnError(throwable -> logFailureGeneration(throwable, refreshToken));
    }

    private void logSuccessGeneration(Object state) {
        log.info("Successful generation of auth token", state);
    }

    private void logFailureGeneration(Throwable throwable, Object state) {
        log.error("Failure generation of auth token", throwable, state);
    }
}
