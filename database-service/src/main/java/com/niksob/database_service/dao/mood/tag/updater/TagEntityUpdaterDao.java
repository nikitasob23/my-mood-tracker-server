package com.niksob.database_service.dao.mood.tag.updater;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;
import com.niksob.database_service.model.mood.tag.user.UserMoodTagEntities;

import java.util.Set;

public interface TagEntityUpdaterDao {
    UserMoodTagEntities save(MoodTagEntity moodTag);

    UserMoodTagEntities update(MoodTagEntity moodTag);

    UserMoodTagEntities mergeAll(Set<MoodTagEntity> moodTags);

    UserMoodTagEntities deleteById(MoodTagEntity moodTag);

    UserMoodTagEntities deleteAllByUserId(Long userId);
}
