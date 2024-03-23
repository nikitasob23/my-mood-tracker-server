package com.niksob.database_service.dao.auth.token.updater;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.domain.model.auth.token.details.AuthTokenEntityDetails;

public interface AuthTokenEntityUpdaterDao {
    AuthTokenEntity save(AuthTokenEntity authToken);

    AuthTokenEntity update(AuthTokenEntity authToken);

    AuthTokenEntity delete(AuthTokenEntityDetails authTokenDetails);
}
