package com.niksob.database_service.dao.auth.token.existence;

import com.niksob.database_service.model.auth.token.details.AuthTokenEntityDetails;

public interface AuthTokenEntityExistenceDao {
    boolean exists(AuthTokenEntityDetails authTokenEntityDetails);
}
