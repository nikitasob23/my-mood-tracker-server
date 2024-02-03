package com.niksob.database_service.service.user;

import com.niksob.database_service.dao.user.UserDao;
import com.niksob.database_service.util.async.MonoAsyncUtil;
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
        return MonoAsyncUtil.create(() -> userDao.load(username))
                .doOnNext(userInfo -> log.debug("Get user info from user DAO", userInfo));
    }

    @Override
    public Mono<UserInfo> save(UserInfo userInfo) {
        return MonoAsyncUtil.create(() -> userDao.save(userInfo))
                .doOnNext(u -> log.debug("Save user info to user DAO", u));
    }

    @Override
    public Mono<Void> update(UserInfo userInfo) {
        return MonoAsyncUtil.create(() -> userDao.update(userInfo))
                .then()
                .doOnSuccess(ignore -> log.debug("Update user info to user DAO", userInfo));
    }

    @Override
    public Mono<Void> delete(Username username) {
        return MonoAsyncUtil.create(() -> userDao.delete(username))
                .doOnSuccess(ignore -> log.debug("Delete user info from user DAO", username));
    }
}
