package com.niksob.database_service.service.user;

import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;

public interface UserService {
    UserInfo load(Username username);

    UserInfo save(UserInfo userInfo);

    UserInfo update(UserInfo userInfo);

    UserInfo delete(Username username);
}
