package com.niksob.database_service.dao.user.updater;

import com.niksob.database_service.entity.user.UserEntity;

public interface UserEntityUpdaterDao {
    UserEntity save(UserEntity userEntity);

    UserEntity update(UserEntity userEntity);

    UserEntity delete(UserEntity user);
}
