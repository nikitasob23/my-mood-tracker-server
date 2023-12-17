package com.niksob.mapping_wrapper.model;

public interface UserEntityDao extends CacheCleaner {
    UserEntity load(String username);

    void save(UserEntity userEntity);

    UserEntity update(UserEntity userEntity);

    UserEntity delete(String username);

    UserEntity getCurrentUser();

    String get(String username);
}
