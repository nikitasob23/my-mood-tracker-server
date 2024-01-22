package com.niksob.database_service.service.mood.tag;

import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.mood.tag.MoodTagName;
import reactor.core.publisher.Mono;

public interface MoodTagService {
    Mono<MoodTag> load(MoodTagName name);

    Mono<MoodTag> delete(MoodTagName name);

    boolean exists(MoodTagName name);
}
