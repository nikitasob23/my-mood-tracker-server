package com.niksob.database_service.dao.auth.token;

import com.niksob.database_service.entity.user.token.refresh.AuthTokenEntity;

public interface AuthTokenEntityDao {
    AuthTokenEntity save(AuthTokenEntity authToken);
}
