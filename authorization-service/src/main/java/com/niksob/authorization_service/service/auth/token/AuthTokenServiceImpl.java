package com.niksob.authorization_service.service.auth.token;

import com.niksob.authorization_service.service.auth.token.error.handler.AuthTokenServiceErrorHandler;
import com.niksob.authorization_service.service.auth.token.generator.AuthTokenGenerator;
import com.niksob.authorization_service.service.password_encoder.PasswordEncoderService.PasswordEncoderService;
import com.niksob.domain.http.connector.UserDatabaseConnector;
import com.niksob.domain.model.auth.login.RowLoginInDetails;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.domain.model.user.Password;
import com.niksob.domain.model.user.RowPassword;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final UserDatabaseConnector userDatabaseConnector;

    private final AuthTokenGenerator authTokenGenerator;
    private final PasswordEncoderService passwordEncoderService;

    private final AuthTokenServiceErrorHandler errorHandler;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenServiceImpl.class);

    @Override
    public Mono<AuthToken> generate(RowLoginInDetails rowLoginInDetails) {
        return userDatabaseConnector.load(rowLoginInDetails.getUsername())
                .filter(userInfo -> passwordMatches(rowLoginInDetails, userInfo))
                .flatMap(authTokenGenerator::generate)
                .doOnNext(authToken -> log.info("Successful generation of auth token", null))
                .onErrorResume(throwable -> errorHandler.createGeneratingError(throwable, rowLoginInDetails));
    }

    private boolean passwordMatches(RowLoginInDetails rowLoginInDetails, UserInfo userInfo) {
        final RowPassword rowPassword = rowLoginInDetails.getRowPassword();
        final Password encodedPassword = userInfo.getPassword();
        return passwordEncoderService.matches(rowPassword, encodedPassword);
    }
}
