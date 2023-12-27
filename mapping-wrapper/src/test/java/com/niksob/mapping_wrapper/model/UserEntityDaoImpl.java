package com.niksob.mapping_wrapper.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserEntityDaoImpl implements UserEntityDao, CacheCleaner {
    @Override
    public UserEntity load(String username) {
        return new UserEntity();
    }

    @Override
    public void save(UserEntity userEntity) {

    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        return new UserEntity();
    }

    @Override
    public UserEntity delete(String username) {
        return new UserEntity();
    }

    @Override
    public UserEntity getCurrentUser() {
        return new UserEntity();
    }

    @Override
    public void clearCache() {
    }

    public String get(String username) {
        return username;
    }

    private RuntimeException createEntityNotFoundException(String username) {
        return new RuntimeException("Test exception");
    }
}