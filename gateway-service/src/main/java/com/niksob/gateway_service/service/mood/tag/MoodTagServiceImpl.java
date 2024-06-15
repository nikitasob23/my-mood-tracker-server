package com.niksob.gateway_service.service.mood.tag;

import com.niksob.domain.http.connector.microservice.database.mood.tag.MoodTagDatabaseConnector;
import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MoodTagServiceImpl implements MoodTagService {
    private final MoodTagDatabaseConnector moodTagDatabaseConnector;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodTagServiceImpl.class);
    
    @Override
    public Flux<MoodTag> loadByUserId(UserId userId) {
        return moodTagDatabaseConnector.loadByUserId(userId)
                .doOnNext(ignore -> log.info("Successful mood tags loading", null, userId))
                .doOnError(throwable -> log.error("Failure mood tags loading", throwable, userId));
    }

    @Override
    public Mono<MoodTag> save(MoodTag moodTag) {
        return moodTagDatabaseConnector.save(moodTag)
                .doOnNext(ignore -> log.info("Successful mood tag saving", null, moodTag))
                .doOnError(throwable -> log.error("Failure mood tag saving", throwable, moodTag));
    }

    @Override
    public Mono<Void> update(MoodTag moodTag) {
        return moodTagDatabaseConnector.update(moodTag)
                .doOnSuccess(ignore -> log.info("Successful mood tag updating", null, moodTag))
                .doOnError(throwable -> log.error("Failure mood tag updating", throwable, moodTag));
    }

    @Override
    public Mono<Void> deleteById(MoodTag moodTag) {
        return moodTagDatabaseConnector.deleteById(moodTag)
                .doOnSuccess(ignore -> log.info("Successful mood tag deletion", null, moodTag))
                .doOnError(throwable -> log.error("Failure mood tag deletion", throwable, moodTag));
    }
}
