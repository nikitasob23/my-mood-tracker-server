package com.niksob.database_service.service.user;

import com.niksob.database_service.dao.user.UserDao;
import com.niksob.database_service.service.mood.entry.MoodEntryService;
import com.niksob.database_service.service.mood.tag.MoodTagService;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.database_service.util.date.DefUserDateRangeUtil;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    private final MoodEntryService moodEntryService;
    private final MoodTagService moodTagService;

    private final DefUserDateRangeUtil defUserDateRangeUtil;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Mono<UserInfo> load(Username username) {
        return MonoAsyncUtil.create(() -> userDao.load(username))
                .doOnError(throwable -> log.error("User info loading error", throwable, username))
                .flatMap(u -> MonoAsyncUtil.create(() -> loadMoodEntriesAndTags(u)))
                .doOnNext(userInfo -> log.debug("Get user info from user DAO", userInfo));
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

    private UserInfo loadMoodEntriesAndTags(UserInfo userInfo) {
        final UserId userId = userInfo.getId();
        final UserEntryDateRange defRange = defUserDateRangeUtil.create(userId);
        final Mono<HashSet<MoodEntry>> loadedMoodEntries = moodEntryService.loadByDateRange(defRange)
                .collect(HashSet::new, Set::add);

        final Mono<HashSet<MoodTag>> loadedMoodTags = moodTagService.loadByUserId(userId)
                .collect(HashSet::new, Set::add);

        return new UserInfo(userInfo, loadedMoodEntries.block(), loadedMoodTags.block());
    }
}
