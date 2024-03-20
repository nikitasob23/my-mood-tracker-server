package com.niksob.database_service.dao.mood.tag.loader;

import com.niksob.database_service.entity.mood.tag.UserMoodTagEntities;

public interface TagEntityLoaderDao {
    UserMoodTagEntities loadByUserId(Long userId);
}
