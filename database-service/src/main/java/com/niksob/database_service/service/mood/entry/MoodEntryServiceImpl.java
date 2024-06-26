package com.niksob.database_service.service.mood.entry;

import com.niksob.database_service.dao.mood.MoodEntryWithTagsDao;
import com.niksob.database_service.service.user.existence.UserExistenceService;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.UserMoodEntryId;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class MoodEntryServiceImpl implements MoodEntryService {
    private final MoodEntryWithTagsDao moodEntryWithTagsDao;
    private final UserExistenceService userExistenceService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodEntryServiceImpl.class);

    @Override
    public Flux<MoodEntry> loadByDateRange(UserEntryDateRange userEntryDateRange) {
        return userExistenceService.existsOrThrow(userEntryDateRange.getUserId())
                .flatMap(userExists ->
                        MonoAsyncUtil.create(() -> moodEntryWithTagsDao.loadByDateRange(userEntryDateRange))
                ).doOnNext(moodEntries -> log.debug("Get mood entries from DAO", moodEntries))
                .flatMapMany(Flux::fromIterable)
                .doOnError(throwable -> log.error("Mood entries load error", throwable, userEntryDateRange));
    }

    @Override
    @Transactional
    public Mono<MoodEntry> save(MoodEntry moodEntry) {
        return userExistenceService.existsOrThrow(moodEntry.getUserId())
                .flatMap(userExists -> MonoAsyncUtil.create(() -> moodEntryWithTagsDao.saveEntryWithTags(moodEntry)))
                .doOnNext(ignore -> log.debug("Save mood entry to DAO", moodEntry))
                .doOnError(throwable -> log.error("Mood entry save error", throwable, moodEntry));
    }

    @Override
    @Transactional
    public Mono<Void> update(MoodEntry moodEntry) {
        return userExistenceService.existsOrThrow(moodEntry.getUserId())
                .flatMap(userExists -> MonoAsyncUtil.create(() -> moodEntryWithTagsDao.updateEntryWithTags(moodEntry)))
                .then()
                .doOnSuccess(ignore -> log.debug("Update mood entry to DAO", moodEntry))
                .doOnError(throwable -> log.error("Mood entry update error", throwable , moodEntry));
    }

    @Override
    public Mono<Void> deleteByIdAndUserId(UserMoodEntryId userEntryId) {
        return MonoAsyncUtil.create(() -> moodEntryWithTagsDao.deleteByIdAndUserId(userEntryId))
                .doOnSuccess(ignore -> log.debug("Delete mood entry from DAO", userEntryId));
    }
}
