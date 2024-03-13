package com.niksob.database_service.service.user;

import com.niksob.database_service.dao.user.UserDao;
import com.niksob.database_service.service.mood.entry.MoodEntryService;
import com.niksob.database_service.service.mood.tag.loader.MoodTagLoader;
import com.niksob.database_service.service.user.loader.UserLoaderImpl;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.database_service.util.date.DefUserDateRangeUtil;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl extends UserLoaderImpl implements UserService {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(
            UserDao userDao,
            MoodEntryService moodEntryService,
            MoodTagLoader moodTagLoader,
            DefUserDateRangeUtil defUserDateRangeUtil
    ) {
        super(userDao, moodEntryService, moodTagLoader, defUserDateRangeUtil);
    }

    @Override
    public Mono<UserInfo> save(UserInfo userInfo) {
        return MonoAsyncUtil.create(() -> userDao.save(userInfo))
                .doOnNext(u -> log.debug("Save user info to user DAO", u))
                .doOnError(throwable -> log.error("User info saving error", throwable, userInfo));
    }

    @Override
    public Mono<Void> update(UserInfo userInfo) {
        return MonoAsyncUtil.create(() -> userDao.update(userInfo))
                .then()
                .doOnSuccess(ignore -> log.debug("Update user info to user DAO", userInfo))
                .doOnError(throwable -> log.error("User info updating error", throwable, userInfo));
    }

    @Override
    public Mono<Void> delete(Username username) {
        return MonoAsyncUtil.create(() -> userDao.delete(username))
                .doOnSuccess(ignore -> log.debug("Delete user info from user DAO", username))
                .doOnError(throwable -> log.error("User info deleting error", throwable, username));
    }
}
