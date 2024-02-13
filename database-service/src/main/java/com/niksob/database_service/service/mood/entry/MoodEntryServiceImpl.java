package com.niksob.database_service.service.mood.entry;

import com.niksob.database_service.dao.mood.entry.MoodEntryDao;
import com.niksob.database_service.service.mood.tag.MoodTagService;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.MoodEntryId;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class MoodEntryServiceImpl implements MoodEntryService {
    private final MoodEntryDao moodEntryDao;
    private final MoodTagService moodTagService;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodEntryServiceImpl.class);

    @Override
    public Flux<MoodEntry> loadByDateRange(UserEntryDateRange userEntryDateRange) {
        return MonoAsyncUtil.create(() -> moodEntryDao.loadByDateRange(userEntryDateRange))
                .doOnNext(moodEntries -> log.debug("Get mood entries from DAO", moodEntries))
                .flatMapMany(Flux::fromIterable)
                .doOnError(throwable -> log.error("Mood entries load error", throwable, userEntryDateRange));
    }

    @Override
    public Mono<MoodEntry> save(MoodEntry moodEntry) {
        return MonoAsyncUtil.create(() -> moodEntry)
                .flatMap(this::mergeMoodTagsInDao)
                .map(moodEntryDao::save)
                .doOnNext(ignore -> log.debug("Save mood entry to DAO", moodEntry))
                .doOnError(throwable -> log.error("Mood entry save error", throwable, moodEntry));
    }

    @Override
    public Mono<Void> update(MoodEntry moodEntry) {
        return MonoAsyncUtil.create(() -> moodEntry)
                .flatMap(this::mergeMoodTagsInDao)
                .doOnNext(moodEntryDao::update)
                .then()
                .doOnSuccess(ignore -> log.debug("Update mood entry to DAO", moodEntry));
    }

    @Override
    public Mono<Void> deleteById(MoodEntryId id) {
        return MonoAsyncUtil.create(() -> moodEntryDao.deleteById(id))
                .doOnSuccess(ignore -> log.debug("Delete mood entry from DAO", id));
    }

    private Mono<MoodEntry> mergeMoodTagsInDao(MoodEntry moodEntry) {
        return moodTagService.mergeAll(moodEntry.getMoodTags())
                .doOnNext(mergedMoodTags -> log.debug("Merged mood tags in DAO", mergedMoodTags))
                .map(mergedMoodTags -> new MoodEntry(moodEntry, mergedMoodTags));
    }
}
