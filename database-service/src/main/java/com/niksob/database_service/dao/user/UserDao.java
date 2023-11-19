package com.niksob.database_service.dao.user;

import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;

public interface UserDao {
    UserInfo load(Username username);

    void save(UserInfo userInfo);
}
