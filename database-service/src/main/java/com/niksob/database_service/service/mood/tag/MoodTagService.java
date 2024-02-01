package com.niksob.database_service.service.mood.tag;

import com.niksob.domain.model.mood.tag.MoodTag;
import reactor.core.publisher.Mono;

public interface MoodTagService {
    Mono<MoodTag> save(MoodTag moodTag);
}
