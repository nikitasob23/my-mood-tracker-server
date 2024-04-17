package com.niksob.gateway_service.service.mood.entry;

import com.niksob.domain.http.connector.microservice.database.mood.entry.MoodEntryDatabaseConnector;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.MoodEntryId;
import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MoodEntryServiceImpl implements MoodEntryService {
    private final MoodEntryDatabaseConnector moodEntryDatabaseConnector;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(MoodEntryServiceImpl.class);

    @Override
    public Flux<MoodEntry> loadByDateRange(UserEntryDateRange userEntryDateRange) {
        return moodEntryDatabaseConnector.loadByDateRange(userEntryDateRange)
                .doOnNext(ignore -> log.info("Successful mood entry loading", null, userEntryDateRange))
                .doOnError(throwable -> log.error("Failure mood entry loading", throwable, userEntryDateRange));
    }

    @Override
    public Mono<MoodEntry> save(MoodEntry moodEntry) {
        return moodEntryDatabaseConnector.save(moodEntry)
                .doOnNext(ignore -> log.info("Successful mood entry saving", null, moodEntry))
                .doOnError(throwable -> log.error("Failure mood entry saving", throwable, moodEntry));
    }

    @Override
    public Mono<Void> update(MoodEntry moodEntry) {
        return moodEntryDatabaseConnector.update(moodEntry)
                .doOnSuccess(ignore -> log.info("Successful mood entry updating", null, moodEntry))
                .doOnError(throwable -> log.error("Failure mood entry updating", throwable, moodEntry));
    }

    @Override
    public Mono<Void> deleteById(MoodEntryId id) {
        return moodEntryDatabaseConnector.deleteById(id)
                .doOnSuccess(ignore -> log.info("Successful mood entry deletion", null, id))
                .doOnError(throwable -> log.error("Failure mood entry deletion", throwable, id));
    }
}
