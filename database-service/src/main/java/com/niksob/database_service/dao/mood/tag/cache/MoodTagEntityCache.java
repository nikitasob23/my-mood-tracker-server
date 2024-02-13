package com.niksob.database_service.dao.mood.tag.cache;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import lombok.NonNull;

import java.util.Collection;

public interface MoodTagEntityCache {
    String MOOD_TAG_BY_USER_ID_CACHE_NAME = "mood_tags";

    void updateCacheCollection(@NonNull MoodTagEntity newMoodTag);

    void updateCacheCollection(Collection<MoodTagEntity> moodTags);

    void deleteFromCacheCollection(@NonNull MoodTagEntity deletedMoodTag);
}
