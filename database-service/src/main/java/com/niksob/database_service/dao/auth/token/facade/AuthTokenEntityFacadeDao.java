package com.niksob.database_service.dao.auth.token.facade;

import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.domain.model.auth.token.details.AuthTokenEntityDetails;

public interface AuthTokenEntityFacadeDao {
    boolean existsByDetails(AuthTokenEntityDetails authTokenDetails);

    boolean exists(AuthTokenEntity authToken);

    AuthTokenEntity load(AuthTokenEntityDetails authTokenEntityDetails);

    AuthTokenEntity save(AuthTokenEntity authToken);

    void update(AuthTokenEntity authToken);

    void delete(AuthTokenEntityDetails authTokenDetails);
}
