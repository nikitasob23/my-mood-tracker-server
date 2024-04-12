package com.niksob.database_service.dao.auth.token.updater;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;

import java.util.Set;

public interface AuthTokenEntityUpdaterDao {
    Set<AuthTokenEntity> save(AuthTokenEntity authToken);

    Set<AuthTokenEntity> update(AuthTokenEntity authToken);

    Set<AuthTokenEntity> delete(AuthTokenEntity authToken);

    Set<AuthTokenEntity> deleteByUserId(Long userId);
}
