package com.niksob.database_service.service.mood.tag;

import com.niksob.database_service.service.mood.tag.loader.MoodTagLoader;
import com.niksob.domain.model.mood.tag.MoodTag;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface MoodTagService extends MoodTagLoader {
    Mono<MoodTag> save(MoodTag moodTag);

    Mono<Void> update(MoodTag moodTag);

    Mono<Set<MoodTag>> mergeAll(Set<MoodTag> moodTags);

    Mono<Void> deleteById(MoodTag moodTag);
}
