package com.niksob.domain.http.connector.microservice.database.mood.entry;

import com.niksob.domain.http.connector.microservice.database.mood.entry.dto.MoodEntryDatabaseDtoConnector;
import com.niksob.domain.mapper.dto.mood.entry.MoodEntryDtoMapper;
import com.niksob.domain.mapper.dto.mood.entry.UserEntryDateRangeDtoMapper;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import com.niksob.domain.model.mood.entry.UserMoodEntryId;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@LayerConnector(source = MoodEntryDatabaseDtoConnector.class, mapper = {
        MoodEntryDtoMapper.class, UserEntryDateRangeDtoMapper.class
})
public interface MoodEntryDatabaseConnector {
    Flux<MoodEntry> loadByDateRange(UserEntryDateRange userEntryDateRange);

    Mono<MoodEntry> save(MoodEntry moodEntry);

    Mono<Void> update(MoodEntry moodEntry);

    Mono<Void> deleteByIdAndUserId(UserMoodEntryId id);
}
