package com.niksob.gateway_service.service.auth;

import com.niksob.domain.http.connector.microservice.database.user.UserDatabaseConnector;
import com.niksob.domain.model.user.User;
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
    public Mono<User> loadByUsername(Username username) {
        return userDatabaseConnector.load(username)
                .doOnNext(ignore -> log.info("Successful user loading", username))
                .doOnError(throwable -> log.error("Failure user loading", throwable, username));
    }

    @Override
    public Mono<UserInfo> loadFullByUsername(Username username) {
        return userDatabaseConnector.loadFull(username)
                .doOnNext(ignore -> log.info("Successful full user loading", username))
                .doOnError(throwable -> log.error("Failure full user loading", throwable, username));
    }

    @Override
    public Mono<UserInfo> save(UserInfo userInfo) {
        return userDatabaseConnector.save(userInfo)
                .doOnNext(ignore -> log.info("Successful user saving", userInfo))
                .doOnError(throwable -> log.error("Failure user saving", throwable, userInfo));
    }

    @Override
    public Mono<Void> update(UserInfo userInfo) {
        return userDatabaseConnector.update(userInfo)
                .doOnSuccess(ignore -> log.info("Successful user updating", userInfo))
                .doOnError(throwable -> log.error("Failure user updating", throwable, userInfo));
    }

    @Override
    public Mono<Void> delete(Username username) {
        return userDatabaseConnector.delete(username)
                .doOnSuccess(ignore -> log.info("Successful user deletion", username))
                .doOnError(throwable -> log.error("Failure user deletion", throwable, username));
    }
}
