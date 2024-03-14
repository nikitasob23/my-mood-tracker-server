package com.niksob.database_service.dao.user;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.database_service.dao.user.cached.UserEntityExistenceDao;
import com.niksob.database_service.dao.user.cached.loader.UserEntityLoaderDao;
import com.niksob.database_service.entity.user.UserEntity;

public interface UserEntityDao extends UserEntityExistenceDao, UserEntityLoaderDao, CacheCleaner {
    UserEntity save(UserEntity userEntity);

    UserEntity update(UserEntity userEntity);

    void delete(String username);
}
