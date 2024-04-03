package com.niksob.authorization_service.service.auth.token;

import com.niksob.authorization_service.exception.auth.token.invalid.InvalidAuthTokenException;
import com.niksob.authorization_service.service.auth.login.LoginInService;
import com.niksob.authorization_service.service.auth.token.generator.AuthTokenAdapter;
import com.niksob.authorization_service.service.auth.token.saver.AuthTokenRepoService;
import com.niksob.authorization_service.service.encoder.auth_token.AuthTokenEncodingService;
import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.auth.token.RefreshToken;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.user.UserId;
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

    private final AuthTokenEncodingService authTokenEncodingService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenServiceImpl.class);

    @Override
    public Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails) {
        return loginInService.loginInOrThrow(rowLoginInDetails)
                .map(user -> new AuthTokenDetails(user.getUsername(), user.getId(), rowLoginInDetails.getDevice()))
                .flatMap(authTokenAdapter::generate)
                .flatMap(authTokenRepoService::upsert)
                .doOnNext(this::logSuccessGeneration)
                .doOnError(throwable -> logFailureGeneration(throwable, rowLoginInDetails));
    }

    @Override
    public Mono<AuthToken> generateByRefresh(RefreshToken refreshToken) {
        return authTokenAdapter.extractAuthTokenDetails(refreshToken) // if expired then throw exception
                .flatMap(authTokenDetails -> validOrThrow(authTokenDetails, refreshToken))
                .flatMap(authTokenAdapter::generate)
                .flatMap(authTokenRepoService::update)
                .doOnNext(this::logSuccessGeneration)
                .doOnError(throwable -> logFailureGeneration(throwable, refreshToken));
    }

    @Override
    public Mono<Void> invalidate(AuthTokenDetails authTokenDetails) {
        return authTokenRepoService.delete(authTokenDetails)
                .doOnSuccess(ignore -> log.info("Successful deletion of auth token", authTokenDetails))
                .doOnError(throwable -> log.error("Failure deletion of auth token", throwable, authTokenDetails));
    }

    @Override
    public Mono<Void> invalidateByUserId(UserId userId) {
        return authTokenRepoService.deleteByUserId(userId)
                .doOnSuccess(ignore -> log.info("Successful deletion auth tokens for all devices", userId))
                .doOnError(t -> log.error("Failure deletion auth tokens for all devices", null, userId));
    }

    private Mono<AuthTokenDetails> validOrThrow(AuthTokenDetails authTokenDetails, RefreshToken refreshToken) {
        return authTokenRepoService.load(authTokenDetails)
                .filter(encoded -> authTokenEncodingService.matches(refreshToken, encoded.getRefresh()))
                .map(ignore -> authTokenDetails)
                .switchIfEmpty(createInvalidTokenException(null, authTokenDetails))
                .onErrorResume(throwable -> createInvalidTokenException(throwable, authTokenDetails));
    }

    private <T> Mono<T> createInvalidTokenException(Throwable throwable, Object state) {
        final String message = "Invalid authorization token";
        var e = new InvalidAuthTokenException(message, throwable);
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
