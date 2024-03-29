package com.niksob.database_service.dao.auth.token.updater;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;

public interface AuthTokenEntityUpdaterDao {
    AuthTokenEntity save(AuthTokenEntity authToken);

    AuthTokenEntity update(AuthTokenEntity authToken);

    AuthTokenEntity delete(AuthTokenEntity authToken);

    void deleteByUserId(Long userId);
}
