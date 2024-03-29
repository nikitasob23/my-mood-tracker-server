package com.niksob.database_service.dao.auth.token.loader;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.model.auth.token.details.AuthTokenEntityDetails;

import java.util.Set;

public interface AuthTokenEntityLoaderDao {
    AuthTokenEntity load(AuthTokenEntityDetails authTokenEntityDetails);

    Set<AuthTokenEntity> loadAllByUserId(Long userId);
}
