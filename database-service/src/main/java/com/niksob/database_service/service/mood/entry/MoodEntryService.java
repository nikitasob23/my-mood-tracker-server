package com.niksob.database_service.service.mood.entry;

import com.niksob.domain.model.mood.entry.UserEntryDateRange;
import com.niksob.domain.model.mood.entry.MoodEntry;
import com.niksob.domain.model.mood.entry.UserMoodEntryId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MoodEntryService {
    Flux<MoodEntry> loadByDateRange(UserEntryDateRange userEntryDateRange);

    Mono<MoodEntry> save(MoodEntry moodEntry);

    Mono<Void> update(MoodEntry moodEntry);

    Mono<Void> deleteByIdAndUserId(UserMoodEntryId userEntryId);
}
