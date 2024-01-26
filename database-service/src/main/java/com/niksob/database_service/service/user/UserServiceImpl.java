package com.niksob.database_service.service.user;

import com.niksob.database_service.dao.user.UserDao;
import com.niksob.database_service.exception.entity.EntityAlreadyExistsException;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Mono<UserInfo> load(Username username) {
        return Mono.just(username)
                .map(userDao::load)
                .filter(Objects::nonNull)
                .doOnNext(userInfo -> log.debug("Get user info from user DAO", userInfo))
                .onErrorResume(throwable -> createEntityNotFoundException(throwable, username));
    }

    @Override
    public Mono<UserInfo> save(UserInfo userInfo) {
        return Mono.just(userInfo)
                .filter(u -> !exists(u.getUsername()))
                .switchIfEmpty(createUserAlreadyExistsException(userInfo))
                .map(userDao::save)
                .doOnNext(u -> log.debug("Save user info to user DAO", u));
    }

    @Override
    public Mono<UserInfo> update(UserInfo userInfo) {
        return Mono.just(userInfo)
                .filter(u -> existsOrThrowNotFound(u.getUsername()))
                .map(userDao::update)
                .doOnNext(u -> log.debug("Update user info to user DAO", u));
    }

    @Override
    public Mono<UserInfo> delete(Username username) {
        return Mono.just(username)
                .filter(this::existsOrThrowNotFound)
                .map(userDao::load)
                .doOnNext(ignore -> userDao.delete(username))
                .doOnNext(loaded -> log.debug("Deleted user info from user DAO", loaded));
    }

    @Override
    public boolean exists(Username username) {
        return userDao.load(username) != null;
    }

    private boolean existsOrThrowNotFound(Username username) {
        return load(username).blockOptional().isPresent();
    }

    private Mono<UserInfo> createEntityNotFoundException(Throwable throwable, Username username) {
        final EntityNotFoundException e =
                new EntityNotFoundException("Username not found by username", (Exception) throwable);
        log.error("Failed loading user by username from repository", e, username);
        throw e;
    }

    private Mono<UserInfo> createUserAlreadyExistsException(UserInfo userInfo) {
        final String username = userInfo.getUsername().getValue();
        return Mono.error(new EntityAlreadyExistsException("Failed to save user. User already exists", username));
    }
}
