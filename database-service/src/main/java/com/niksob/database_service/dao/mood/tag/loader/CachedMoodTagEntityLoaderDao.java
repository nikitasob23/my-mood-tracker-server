package com.niksob.database_service.dao.mood.tag.loader;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;

import java.util.Set;

public interface CachedMoodTagEntityLoaderDao {
    Set<MoodTagEntity> loadByUserId(Long userId);
}
