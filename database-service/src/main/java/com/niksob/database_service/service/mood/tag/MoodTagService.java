package com.niksob.database_service.service.mood.tag;

import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MoodTagService {
    Flux<MoodTag> loadByUserId(UserId userId);

    Mono<MoodTag> save(MoodTag moodTag);
}
