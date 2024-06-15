package com.niksob.authorization_service.service.user;

import com.niksob.domain.exception.resource.ResourceAlreadyExistsException;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.domain.http.connector.microservice.database.user.UserDatabaseConnector;
import com.niksob.domain.model.user.Email;
import com.niksob.domain.model.user.UserId;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDatabaseConnector userDatabaseConnector;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Mono<Boolean> existsByEmailOrThrow(Email email) {
        return userDatabaseConnector.existsByEmail(email)
                .flatMap(exists -> exists ? createUserAlreadyExistsError("Email", email) : Mono.just(false));
    }

    @Override
    public Mono<Boolean> existsByUsernameOrThrow(Username username) {
        return userDatabaseConnector.load(username)
                .map(u -> Boolean.TRUE)
                .onErrorResume(throwable -> {
                    if (throwable instanceof ResourceNotFoundException) {
                        return Mono.just(Boolean.FALSE);
                    }
                    return createUserAlreadyExistsError("Username", username);
                });
    }

    @Override
    public Mono<UserInfo> loadById(UserId userId) {
        return userDatabaseConnector.loadById(userId)
                .doOnNext(user -> log.info("User was loaded", user));
    }

    @Override
    public Mono<UserInfo> save(UserInfo userInfo) {
        return userDatabaseConnector.save(userInfo)
                .doOnNext(user -> log.info("User was saved", user));
    }

    @Override
    public Mono<Void> update(UserInfo userInfo) {
        return userDatabaseConnector.update(userInfo)
                .doOnSuccess(ignore -> log.info("User was updated", userInfo));
    }

    private <T> Mono<T> createUserAlreadyExistsError(String marker, Object state) {
        final String message = marker + " already exists";
        var e = new ResourceAlreadyExistsException(message, null, state);
        log.error(message, e, state);
        return Mono.error(e);
    }
}
