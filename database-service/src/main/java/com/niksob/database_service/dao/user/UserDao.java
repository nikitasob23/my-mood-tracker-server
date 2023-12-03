package com.niksob.database_service.dao.user;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;

public interface UserDao extends CacheCleaner {
    UserInfo load(Username username);

    UserInfo save(UserInfo userInfo);
}
