package com.niksob.authorization_service.service.auth.token;

import com.niksob.authorization_service.exception.username.UsernameNotFoundAuthException;
import com.niksob.authorization_service.service.auth.token.generator.AuthTokenGenerator;
import com.niksob.authorization_service.service.password_encoder.PasswordEncoderService.PasswordEncoderService;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.http.connector.UserDatabaseConnector;
import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.user.Password;
import com.niksob.domain.model.user.RowPassword;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import jakarta.security.auth.message.AuthException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final PasswordEncoderService passwordEncoderService;

    private final UserDatabaseConnector userDatabaseConnector;

    private final AuthTokenGenerator authTokenGenerator;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenServiceImpl.class);

    @Override
    public Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails) {
        return userDatabaseConnector.load(rowLoginInDetails.getUsername())
                .filter(userInfo -> passwordMatches(rowLoginInDetails, userInfo))
                .map(authTokenGenerator::generate)
                .onErrorResume(throwable -> createGeneratingError(throwable, rowLoginInDetails));
    }

    private boolean passwordMatches(RowLoginInDetails rowLoginInDetails, UserInfo userInfo) {
        final RowPassword rowPassword = rowLoginInDetails.getRowPassword();
        final Password encodedPassword = userInfo.getPassword();
        return passwordEncoderService.matches(rowPassword, encodedPassword);
    }

    private Mono<AuthToken> createGeneratingError(Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createAuthException(throwable, state);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.NOT_FOUND) == 0) {
            var e = new UsernameNotFoundAuthException("Username not found from database", throwable);
            log.error("Authorization by login in details failed", e, state);
            return Mono.error(e);
        }
        return createAuthException(throwable, state);
    }

    private Mono<AuthToken> createAuthException(Throwable throwable, Object state) {
        final AuthException authException = new AuthException("Authorization failed", throwable);
        log.error("Authorization by login in details failed", authException, state);
        return Mono.error(authException);
    }
}
