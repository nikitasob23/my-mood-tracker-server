package com.niksob.database_service.dao.auth.token.loader;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;

import java.util.Set;

public interface AuthTokenEntityLoaderDao {
    Set<AuthTokenEntity> loadAllByUserId(Long userId);
}
