package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;

import java.util.Set;

public interface MoodTagEntityDao {
    Set<MoodTagEntity> loadByUserId(Long userId);

    MoodTagEntity save(MoodTagEntity moodTag);
}
