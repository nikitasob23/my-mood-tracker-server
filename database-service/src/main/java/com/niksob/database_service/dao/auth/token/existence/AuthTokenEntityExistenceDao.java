package com.niksob.database_service.dao.auth.token.existence;

import com.niksob.domain.model.auth.token.details.AuthTokenEntityDetails;

public interface AuthTokenEntityExistenceDao {
    boolean exists(AuthTokenEntityDetails authTokenEntityDetails);
}
