package com.niksob.database_service.service.mood.tag.loader;

import com.niksob.database_service.dao.mood.tag.MoodTagDao;
import com.niksob.database_service.service.user.existence.UserExistenceService;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class MoodTagLoaderImpl implements MoodTagLoader {
    @Qualifier("userExistenceService")
    protected final UserExistenceService userExistenceService;
    protected final MoodTagDao moodTagDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagLoaderImpl.class);

    @Override
    public Flux<MoodTag> loadByUserId(UserId userId) {
        return userExistenceService.existsOrThrow(userId)
                .flatMapMany(ignore -> MonoAsyncUtil.create(() -> moodTagDao.loadByUserId(userId))
                        .doOnNext(moodTags -> log.debug("Get mood tag from DAO", moodTags))
                        .flatMapMany(Flux::fromIterable)
                        .doOnError(throwable -> log.error("Mood tag load error", throwable, userId)));
    }
}
