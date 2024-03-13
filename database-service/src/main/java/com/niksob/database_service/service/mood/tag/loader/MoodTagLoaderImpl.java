package com.niksob.database_service.service.mood.tag.loader;

import com.niksob.database_service.dao.mood.tag.MoodTagDao;
import com.niksob.database_service.service.user.loader.UserLoader;
import com.niksob.database_service.util.async.MonoAsyncUtil;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class MoodTagLoaderImpl implements MoodTagLoader {
    @Qualifier("userLoader")
    protected final UserLoader userLoader;
    protected final MoodTagDao moodTagDao;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagLoaderImpl.class);

    @Override
    public Flux<MoodTag> loadByUserId(UserId userId) {
        return checkUserExistence(userId)
                .flatMapMany(ignore -> MonoAsyncUtil.create(() -> moodTagDao.loadByUserId(userId))
                        .doOnNext(moodTags -> log.debug("Get mood tag from DAO", moodTags))
                        .flatMapMany(Flux::fromIterable)
                        .doOnError(throwable -> log.error("Mood tag load error", throwable, userId)));
    }

    protected Mono<UserInfo> checkUserExistence(UserId userId) {
        return userLoader.loadById(userId)
                .doOnError(throwable -> log.error("Mood tag user owner is not found", throwable, userId));
    }
}
