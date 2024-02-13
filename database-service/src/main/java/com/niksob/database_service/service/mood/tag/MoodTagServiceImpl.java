package com.niksob.database_service.service.mood.tag;

import com.niksob.database_service.dao.mood.tag.MoodTagDao;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@AllArgsConstructor
public class MoodTagServiceImpl implements MoodTagService {
    private final MoodTagDao moodTagDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagServiceImpl.class);

    @Override
    public Flux<MoodTag> loadByUserId(UserId userId) {
        return MonoAsyncUtil.create(() -> moodTagDao.loadByUserId(userId))
                .doOnNext(moodTags -> log.debug("Get mood tag from DAO", moodTags))
                .flatMapMany(Flux::fromIterable)
                .doOnError(throwable -> log.error("Mood tag load error", throwable, userId));
    }

    @Override
    public Mono<MoodTag> save(MoodTag moodTag) {
        return MonoAsyncUtil.create(() -> moodTagDao.save(moodTag))
                .doOnNext(ignore -> log.debug("Save mood tag to DAO", moodTag))
                .doOnError(throwable -> log.error("Mood tag save error", throwable, moodTag));
    }

    @Override
    public Mono<Void> update(MoodTag moodTag) {
        return MonoAsyncUtil.create(() -> moodTagDao.update(moodTag))
                .then()
                .doOnSuccess(ignore -> log.debug("Update mood tag to DAO", moodTag));
    }

    @Override
    public Mono<Set<MoodTag>> mergeAll(Set<MoodTag> moodTags) {
        return MonoAsyncUtil.create(() -> moodTagDao.mergeAll(moodTags))
                .doOnNext(ignore -> log.debug("Merge mood tag to DAO", moodTags))
                .doOnError(throwable -> log.error("Mood tag merge error", throwable, moodTags));
    }

    @Override
    public Mono<Void> deleteById(MoodTag moodTag) {
        return MonoAsyncUtil.create(() -> moodTagDao.deleteById(moodTag))
                .doOnSuccess(ignore -> log.debug("Delete mood tag from DAO", moodTag));
    }
}
