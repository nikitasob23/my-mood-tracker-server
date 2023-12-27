package com.niksob.mapping_wrapper.model;

import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.mapping_wrapper.annotation.MappingWrapper;

@MappingWrapper(source = UserEntityDaoImpl.class, mapper = UserEntityMapper.class, isSpringComponentEnabled = true)
public interface UserDao {
    UserInfo load(Username username);

    void save(UserInfo UserInfo);

    UserInfo update(UserInfo UserInfo);

    UserInfo delete(Username username);

    String get(String username);

    UserInfo getCurrentUser();
}