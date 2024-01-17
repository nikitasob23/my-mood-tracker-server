package com.niksob.database_service.service.user;

import com.niksob.database_service.dao.user.UserDao;
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
    private final UserDao userDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Mono<UserInfo> load(Username username) {
        return Mono.just(username)
                .map(userDao::load)
                .doOnNext(userInfo -> log.debug("Get user info from user DAO", userInfo));
    }

    @Override
    public Mono<UserInfo> save(UserInfo userInfo) {
        return Mono.just(userInfo)
                .map(userDao::save)
                .doOnNext(u -> log.debug("Save user info to user DAO", u));
    }

    @Override
    public Mono<UserInfo> update(UserInfo userInfo) {
        return Mono.just(userInfo)
                .map(userDao::update)
                .doOnNext(u -> log.debug("Update user info to user DAO", u));
    }

    @Override
    public Mono<UserInfo> delete(Username username) {
        return Mono.just(username)
                .map(userDao::load)
                .doOnNext(ignore -> userDao.delete(username))
                .doOnNext(loaded -> log.debug("Deleted user info from user DAO", loaded));
    }
}
