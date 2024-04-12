package com.niksob.database_service.dao.auth.token.existence;

import com.niksob.database_service.dao.auth.token.loader.AuthTokenEntityLoaderDao;
import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.model.auth.token.details.AuthTokenEntityDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.niksob.database_service.util.token.auth.filter.AuthTokenEntityFilter.filterByDevice;

@Component
@AllArgsConstructor
public class AuthTokenEntityExistenceDaoImpl implements AuthTokenEntityExistenceDao {
    private final AuthTokenEntityLoaderDao loaderDao;

    @Override
    public boolean exists(AuthTokenEntityDetails authTokenEntityDetails) {
        final Set<AuthTokenEntity> userAuthTokenStorage = loaderDao.loadAllByUserId(authTokenEntityDetails.getUserId());
        final AuthTokenEntity token = filterByDevice(userAuthTokenStorage, authTokenEntityDetails.getDevice());
        return token != null;
    }
}
