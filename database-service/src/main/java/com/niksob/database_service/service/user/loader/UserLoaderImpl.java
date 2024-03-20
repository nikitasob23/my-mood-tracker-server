package com.niksob.database_service.service.user.loader;


import com.niksob.database_service.dao.user.UserDao;
import com.niksob.database_service.service.mood.entry.MoodEntryService;
import com.niksob.database_service.service.mood.tag.loader.MoodTagLoader;
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
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@AllArgsConstructor
public class UserLoaderImpl implements UserLoader {
    protected final UserDao userDao;

    protected final MoodEntryService moodEntryService;
    @Qualifier("moodTagLoader")
    protected final MoodTagLoader moodTagLoader;

    protected final DefUserDateRangeUtil defUserDateRangeUtil;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserLoaderImpl.class);

    @Override
    public Mono<UserInfo> loadById(UserId userId) {
        return loadUser(() -> userDao.loadById(userId), false, userId);
    }

    @Override
    public Mono<UserInfo> loadAllById(UserId userId) {
        return loadUser(() -> userDao.loadById(userId), true, userId);
    }

    @Override
    public Mono<UserInfo> loadByUsername(Username username) {
        return loadUser(() -> userDao.loadByUsername(username), false, username);
    }

    @Override
    public Mono<UserInfo> loadAllByUsername(Username username) {
        return loadUser(() -> userDao.loadByUsername(username), true, username);
    }

    private Mono<UserInfo> loadUser(Supplier<UserInfo> loader, boolean necessaryToLoadAllUser, Object logState) {
        return MonoAsyncUtil.create(loader::get)
                .doOnError(throwable -> log.error("User info loading error", throwable, logState))
                .flatMap(userInfo -> necessaryToLoadAllUser ? loadMoodEntriesAndTags(userInfo) : Mono.just(userInfo))
                .doOnNext(this::logLoadingUser);
    }

    private Mono<UserInfo> loadMoodEntriesAndTags(UserInfo userInfo) {
        final UserId userId = userInfo.getId();
        final UserEntryDateRange defRange = defUserDateRangeUtil.create(userId);
        final Mono<HashSet<MoodEntry>> loadedMoodEntries = moodEntryService.loadByDateRange(defRange)
                .collect(HashSet::new, Set::add);

        final Mono<HashSet<MoodTag>> loadedMoodTags = moodTagLoader.loadByUserId(userId)
                .collect(HashSet::new, Set::add);

        return loadedMoodEntries.zipWith(loadedMoodTags, (entries, tags) -> new UserInfo(userInfo, entries, tags));
    }

    private void logLoadingUser(UserInfo userInfo) {
        log.debug("Get user info from user DAO", userInfo);
    }
}
