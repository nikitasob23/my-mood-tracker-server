package com.niksob.database_service.dao.mood.tag.loader;

import com.niksob.database_service.model.mood.tag.user.UserMoodTagEntities;

public interface TagEntityLoaderDao {
    UserMoodTagEntities loadByUserId(Long userId);
}
