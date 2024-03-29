package com.niksob.database_service.dao.auth.token.loader;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.model.auth.token.details.AuthTokenEntityDetails;

public interface AuthTokenEntityLoaderDao {
    AuthTokenEntity load(AuthTokenEntityDetails authTokenEntityDetails);
}
