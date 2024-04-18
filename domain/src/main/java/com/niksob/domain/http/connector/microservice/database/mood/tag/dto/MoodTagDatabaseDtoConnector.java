package com.niksob.domain.http.connector.microservice.database.mood.tag.dto;

import com.niksob.domain.dto.mood.tag.MoodTagDto;
import com.niksob.domain.dto.user.UserIdDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MoodTagDatabaseDtoConnector {
    Flux<MoodTagDto> loadByUserId(UserIdDto userId);

    Mono<MoodTagDto> save(MoodTagDto moodTag);

    Mono<Void> update(MoodTagDto moodTag);

    Mono<Void> deleteById(MoodTagDto moodTag);
}
