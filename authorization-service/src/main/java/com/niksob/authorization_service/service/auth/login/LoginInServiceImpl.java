package com.niksob.authorization_service.service.auth.login;

import com.niksob.authorization_service.exception.auth.signup.UnregisteredUserException;
import com.niksob.authorization_service.model.login.password.WrongPasswordException;
import com.niksob.authorization_service.service.password_encoder.PasswordEncoderService.PasswordEncoderService;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.domain.http.connector.microservice.database.user.UserDatabaseConnector;
import com.niksob.domain.model.auth.login.RowLoginInDetails;
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
public class LoginInServiceImpl implements LoginInService {
    private final UserDatabaseConnector userDatabaseConnector;
    private final PasswordEncoderService passwordEncoderService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(LoginInServiceImpl.class);

    @Override
    public Mono<UserInfo> loginInOrThrow(RowLoginInDetails rowLoginInDetails) {
        return userDatabaseConnector.load(rowLoginInDetails.getUsername())
                .flatMap(user -> passwordMatches(rowLoginInDetails, user))
                .doOnNext(user -> log.info("Successful login in", null, user))
                .onErrorResume(throwable -> createUserNotExistsError(throwable, rowLoginInDetails));
    }

    private Mono<UserInfo> passwordMatches(RowLoginInDetails rowLoginInDetails, UserInfo userInfo) {
        return Mono.just(userInfo)
                .filter(user -> {
                    final RowPassword rowPassword = rowLoginInDetails.getRowPassword();
                    final Password encodedPassword = user.getPassword();
                    return passwordEncoderService.matches(rowPassword, encodedPassword);
                }).switchIfEmpty(createWrongPasswordError(rowLoginInDetails));
    }

    private <T> Mono<T> createUserNotExistsError(Throwable throwable, Object state) {
        Throwable e;
        if (throwable instanceof ResourceNotFoundException) {
            e = new UnregisteredUserException("User doesn't exist", throwable);
        } else {
            e = throwable;
        }
        log.error("Login in failed", e, state);
        return Mono.error(e);
    }

    private <T> Mono<T> createWrongPasswordError(Object state) {
        final String message = "Wrong password";
        var e = new WrongPasswordException(message);
        log.error(message, e, state);
        return Mono.error(e);
    }
}
