package com.niksob.authorization_service.service.auth.token;

import com.niksob.authorization_service.exception.auth.token.invalid.InvalidAuthTokenException;
import com.niksob.authorization_service.service.auth.login.LoginInService;
import com.niksob.authorization_service.service.auth.token.generator.AuthTokenAdapter;
import com.niksob.authorization_service.service.auth.token.saver.AuthTokenRepoService;
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
    private final AuthTokenRepoService authTokenRepoService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenServiceImpl.class);

    @Override
    public Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails) {
        return loginInService.loginInOrThrow(rowLoginInDetails)
                .map(userId -> new AuthTokenDetails(userId, rowLoginInDetails.getDevice()))
                .flatMap(authTokenAdapter::generate)
                .flatMap(authTokenRepoService::upsert)
                .doOnNext(this::logSuccessGeneration)
                .doOnError(throwable -> logFailureGeneration(throwable, rowLoginInDetails));
    }

    @Override
    public Mono<AuthToken> generateByRefresh(RefreshToken refreshToken) {
        return authTokenAdapter.extractAuthTokenDetails(refreshToken) // if expired then throw exception
                .flatMap(this::validOrThrow)
                .flatMap(authTokenAdapter::generate)
                .flatMap(authTokenRepoService::update)
                .doOnNext(this::logSuccessGeneration)
                .doOnError(throwable -> logFailureGeneration(throwable, refreshToken));
    }

    private Mono<AuthTokenDetails> validOrThrow(AuthTokenDetails authTokenDetails) {
        return authTokenRepoService.filterExists(authTokenDetails)
                .switchIfEmpty(createInvalidTokenException(authTokenDetails));
    }

    private <T> Mono<T> createInvalidTokenException(Object state) {
        final String message = "Invalid authorization token";
        var e = new InvalidAuthTokenException(message);
        log.error(message, null, state);
        return Mono.error(e);
    }

    private void logSuccessGeneration(Object state) {
        log.info("Successful generation of auth token", state);
    }

    private void logFailureGeneration(Throwable throwable, Object state) {
        log.error("Failure generation of auth token", throwable, state);
    }
}