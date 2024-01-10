package com.niksob.database_service.dao.user;

import com.niksob.database_service.mapper.dao.user.UserEntityMapper;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.mapping_wrapper.annotation.MappingWrapper;

@MappingWrapper(source = CachedUserEntityDao.class, mapper = UserEntityMapper.class)
public interface UserDao {
    UserInfo load(Username username);

    UserInfo save(UserInfo userInfo);

    UserInfo update(UserInfo userInfo);

    void delete(Username username);
}
