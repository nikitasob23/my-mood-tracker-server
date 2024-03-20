package com.niksob.database_service.service.mood.tag.loader;

import com.niksob.domain.model.mood.tag.MoodTag;
import com.niksob.domain.model.user.UserId;
import reactor.core.publisher.Flux;

public interface MoodTagLoader {
    Flux<MoodTag> loadByUserId(UserId userId);
}
