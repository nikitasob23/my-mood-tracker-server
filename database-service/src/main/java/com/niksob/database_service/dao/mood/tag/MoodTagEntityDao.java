package com.niksob.database_service.dao.mood.tag;

import com.niksob.database_service.entity.mood.tag.MoodTagEntity;

public interface MoodTagEntityDao {
    MoodTagEntity load(String name);

    void delete(String name);
}
