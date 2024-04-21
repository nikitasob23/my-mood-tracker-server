package com.niksob.database_service.dao.user.facade;

import com.niksob.database_service.entity.user.UserEntity;

public interface UserEntityDaoFacade {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsById(Long id);

    UserEntity loadById(Long id);

    UserEntity loadByUsername(String username);

    UserEntity save(UserEntity userEntity);

    UserEntity update(UserEntity userEntity);

    void delete(String username);
}
