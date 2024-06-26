package com.niksob.database_service.service.user.existence;

import com.niksob.database_service.dao.user.UserDao;
import com.niksob.database_service.service.mood.entry.MoodEntryService;
import com.niksob.database_service.service.mood.tag.loader.MoodTagLoader;
import com.niksob.database_service.service.user.loader.UserLoaderImpl;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.database_service.util.date.DefUserDateRangeUtil;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.domain.model.user.Email;
import com.niksob.domain.model.user.UserId;
import com.niksob.domain.model.user.Username;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserExistenceServiceImpl extends UserLoaderImpl implements UserExistenceService {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserExistenceServiceImpl.class);

    public UserExistenceServiceImpl(
            UserDao userDao,
            MoodEntryService moodEntryService,
            MoodTagLoader moodTagLoader,
            DefUserDateRangeUtil defUserDateRangeUtil
    ) {
        super(userDao, moodEntryService, moodTagLoader, defUserDateRangeUtil);
    }

    @Override
    public Mono<Boolean> existsByEmail(Email email) {
        return MonoAsyncUtil.create(() -> userDao.existsByEmail(email))
                .doOnNext(existence -> log.info("The user exists", null, email))
                .doOnError(throwable -> log.error("Failed getting user existence by email", throwable, email));
    }

    @Override
    public Mono<Boolean> existsByUsername(Username username) {
        return MonoAsyncUtil.create(() -> userDao.existsByUsername(username))
                .doOnNext(existence -> log.info("The user exists", null, username))
                .doOnError(throwable -> log.error("Failed getting user existence by username", throwable, username));
    }

    @Override
    public Mono<Boolean> existsById(UserId id) {
        return MonoAsyncUtil.create(() -> userDao.existsById(id))
                .doOnError(throwable -> log.error("Failed getting user existence by id", throwable, id));
    }

    @Override
    public Mono<Boolean> existsOrThrow(UserId userId) {
        return existsById(userId)
                .flatMap(existence -> existence ?
                        Mono.just(existence) : Mono.error(new ResourceNotFoundException("User not found", null, userId))
                ).doOnError(throwable -> log.error("Mood tag user owner is not found", throwable, userId));
    }
}
