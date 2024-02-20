package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.dao.mood.tag.loader.MoodTagEntityLoaderDao;
import com.niksob.database_service.entity.mood.tag.MoodTagEntity;

import java.util.Set;

public interface MoodTagEntityDao extends MoodTagEntityLoaderDao {
    MoodTagEntity save(MoodTagEntity moodTag);

    MoodTagEntity update(MoodTagEntity moodTag);

    Set<MoodTagEntity> mergeAll(Set<MoodTagEntity> moodTags);

    void deleteById(MoodTagEntity moodTag);
}
