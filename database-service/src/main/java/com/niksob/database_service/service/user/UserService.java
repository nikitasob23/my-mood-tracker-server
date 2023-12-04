package com.niksob.database_service.service.user;

import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;

public interface UserService {
    UserInfo load(Username username);

    void save(UserInfo userInfo);

    void update(UserInfo userInfo);

    void delete(Username username);
}
