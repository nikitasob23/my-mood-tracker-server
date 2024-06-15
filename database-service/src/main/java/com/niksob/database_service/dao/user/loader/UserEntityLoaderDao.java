package com.niksob.database_service.dao.user.loader;

import com.niksob.database_service.entity.user.UserEntity;

public interface UserEntityLoaderDao {
    UserEntity loadById(Long id);

    UserEntity loadByUsername(String username);

    UserEntity loadByEmail(String email);
}
