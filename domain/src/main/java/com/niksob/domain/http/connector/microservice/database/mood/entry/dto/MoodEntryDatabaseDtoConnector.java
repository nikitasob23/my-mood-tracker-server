package com.niksob.domain.http.connector.microservice.database.mood.entry.dto;

import com.niksob.domain.dto.mood.entry.MoodEntryDto;
import com.niksob.domain.dto.mood.entry.UserEntryDateRangeDto;
import com.niksob.domain.dto.mood.entry.UserMoodEntryIdDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MoodEntryDatabaseDtoConnector {
    Flux<MoodEntryDto> loadByDateRange(UserEntryDateRangeDto userEntryDateRange);

    Mono<MoodEntryDto> save(MoodEntryDto moodEntry);

    Mono<Void> update(MoodEntryDto moodEntry);

    Mono<Void> deleteByIdAndUserId(UserMoodEntryIdDto userEntryId);
}
