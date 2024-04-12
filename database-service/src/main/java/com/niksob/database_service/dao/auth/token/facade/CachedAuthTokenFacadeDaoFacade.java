package com.niksob.database_service.dao.auth.token.facade;

import com.niksob.database_service.dao.auth.token.existence.AuthTokenEntityExistenceDao;
import com.niksob.database_service.dao.auth.token.loader.AuthTokenEntityLoaderDao;
import com.niksob.database_service.dao.auth.token.updater.AuthTokenEntityUpdaterDao;
import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.handler.exception.DaoExceptionHandler;
import com.niksob.database_service.mapper.entity.auth.token.details.AuthTokenEntityDetailsMapper;
import com.niksob.database_service.model.auth.token.details.AuthTokenEntityDetails;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.niksob.database_service.util.token.auth.filter.AuthTokenEntityFilter.filterByDevice;

@AllArgsConstructor
public class CachedAuthTokenFacadeDaoFacade implements AuthTokenEntityFacadeDao {
    private final AuthTokenEntityLoaderDao loaderDao;
    private final AuthTokenEntityExistenceDao existenceDao;
    private final AuthTokenEntityUpdaterDao updaterDao;

    private final Cache cache;

    private final AuthTokenEntityDetailsMapper authTokenDetailsMapper;

    private final DaoExceptionHandler exceptionHandler;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedAuthTokenFacadeDaoFacade.class);

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
        final Set<AuthTokenEntity> userAuthTokenStorage = loaderDao.loadAllByUserId(authTokenDetails.getUserId());
        if (userAuthTokenStorage == null || userAuthTokenStorage.isEmpty()) {
            throw exceptionHandler.createResourceNotFoundException(authTokenDetails);
        }
        return filterByDevice(userAuthTokenStorage, authTokenDetails.getDevice());
    }

    @Override
    @Transactional
    public AuthTokenEntity save(AuthTokenEntity authToken) {
        if (exists(authToken)) {
            throw exceptionHandler.createResourceAlreadyExistsException(authToken);
        }
        final Set<AuthTokenEntity> userAuthTokenStorage = updaterDao.save(authToken);
        return filterByDevice(userAuthTokenStorage, authToken.getDevice());
    }

    @Override
    @Transactional
    public AuthTokenEntity update(AuthTokenEntity authToken) {
        if (!exists(authToken)) {
            throw exceptionHandler.createResourceNotFoundException(authToken.getId());
        }
        final Set<AuthTokenEntity> userAuthTokenStorage = updaterDao.update(authToken);
        return filterByDevice(userAuthTokenStorage, authToken.getDevice());
    }

    @Override
    @Transactional
    public void delete(AuthTokenEntityDetails authTokenDetails) {
        if (!existsByDetails(authTokenDetails)) {
            throw exceptionHandler.createResourceNotFoundException(authTokenDetails);
        }
        final AuthTokenEntity authToken = load(authTokenDetails);
        updaterDao.delete(authToken);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        final Set<AuthTokenEntity> authTokens = loaderDao.loadAllByUserId(userId); // non cached
        if (authTokens != null && authTokens.isEmpty()) {
            throw exceptionHandler.createResourceNotFoundException(userId);
        }
        deleteAllByUserIdFromCache(authTokens);
        updaterDao.deleteByUserId(userId);
    }

    private void deleteAllByUserIdFromCache(Set<AuthTokenEntity> authTokens) {
        log.info("Start deleting all user's auth tokens from cache", authTokens);
        authTokens.stream()
                .map(token -> token.getUserId() + token.getDevice())
                .forEach(cacheKey -> cache.put(cacheKey, null));
        log.info("Successful deletion all user's auth tokens from cache", authTokens);
    }
}
