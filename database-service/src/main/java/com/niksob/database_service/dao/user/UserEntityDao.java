package com.niksob.database_service.dao.user;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.database_service.entity.user.UserEntity;

public interface UserEntityDao extends CacheCleaner {
    UserEntity load(String username);

    UserEntity save(UserEntity userEntity);

    UserEntity update(UserEntity userEntity);

    void delete(String username);
}
