package com.niksob.database_service.dao.auth.token.existence;

import com.niksob.database_service.dao.auth.token.loader.AuthTokenEntityLoaderDao;
import com.niksob.domain.model.auth.token.details.AuthTokenEntityDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthTokenEntityExistenceDaoImpl implements AuthTokenEntityExistenceDao {
    private final AuthTokenEntityLoaderDao loaderDao;

    @Override
    public boolean exists(AuthTokenEntityDetails authTokenEntityDetails) {
        return loaderDao.load(authTokenEntityDetails) != null;
    }
}
