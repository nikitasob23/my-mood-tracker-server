package com.niksob.database_service.dao.auth.token.facade;

import com.niksob.database_service.dao.auth.token.existence.AuthTokenEntityExistenceDao;
import com.niksob.database_service.dao.auth.token.loader.AuthTokenEntityLoaderDao;
import com.niksob.database_service.dao.auth.token.updater.AuthTokenEntityUpdaterDao;
import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.handler.exception.DaoExceptionHandler;
import com.niksob.database_service.mapper.entity.auth.token.details.AuthTokenEntityDetailsMapper;
import com.niksob.domain.model.auth.token.details.AuthTokenEntityDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CachedAuthTokenFacadeDaoFacade implements AuthTokenEntityFacadeDao {
    private final AuthTokenEntityLoaderDao loaderDao;
    private final AuthTokenEntityExistenceDao existenceDao;
    private final AuthTokenEntityUpdaterDao updaterDao;

    private final AuthTokenEntityDetailsMapper authTokenDetailsMapper;

    @Qualifier("authTokenDaoExceptionHandler")
    private final DaoExceptionHandler exceptionHandler;

    @Override
    public boolean existsByDetails(AuthTokenEntityDetails authTokenDetails) {
        return existenceDao.exists(authTokenDetails);
    }

    @Override
    public boolean exists(AuthTokenEntity authToken) {
        final AuthTokenEntityDetails authTokenDetails = authTokenDetailsMapper.fromAuthTokenEntity(authToken);
        return existenceDao.exists(authTokenDetails);
    }

    @Override
    public AuthTokenEntity load(AuthTokenEntityDetails authTokenDetails) {
        final AuthTokenEntity authToken = loaderDao.load(authTokenDetails);
        if (authToken == null) {
            throw exceptionHandler.createResourceNotFoundException(authTokenDetails);
        }
        return authToken;
    }

    @Override
    public AuthTokenEntity save(AuthTokenEntity authToken) {
        if (exists(authToken)) {
            throw exceptionHandler.createResourceAlreadyExistsException(authToken);
        }
        return updaterDao.save(authToken);
    }

    @Override
    public void update(AuthTokenEntity authToken) {
        if (!exists(authToken)) {
            throw exceptionHandler.createResourceNotFoundException(authToken.getId());
        }
        updaterDao.update(authToken);
    }

    @Override
    public void delete(AuthTokenEntityDetails authTokenDetails) {
        if (!existsByDetails(authTokenDetails)) {
            throw exceptionHandler.createResourceNotFoundException(authTokenDetails);
        }
        updaterDao.delete(authTokenDetails);
    }
}
