package com.niksob.database_service.dao.user;

import com.niksob.database_service.mapper.entity.user.UserEntityMapper;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.layer_connector.annotation.LayerConnector;

@LayerConnector(source = UserEntityDao.class, mapper = UserEntityMapper.class)
public interface UserDao {
    UserInfo load(Username username);

    UserInfo save(UserInfo userInfo);

    UserInfo update(UserInfo userInfo);

    void delete(Username username);
}
