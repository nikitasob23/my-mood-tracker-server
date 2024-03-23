package com.niksob.database_service.dao.auth.token.updater;

import com.niksob.database_service.dao.auth.token.values.AuthTokenCacheNames;
import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.repository.auth.token.AuthTokenRepository;
import com.niksob.domain.exception.resource.ResourceDeletionException;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.exception.resource.ResourceUpdatingException;
import com.niksob.domain.model.auth.token.details.AuthTokenEntityDetails;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class CachedAuthTokenUpdaterDao implements AuthTokenEntityUpdaterDao {
    private final AuthTokenRepository repository;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedAuthTokenUpdaterDao.class);

    @Override
    @Transactional
    @CachePut(value = AuthTokenCacheNames.USER_ID_AND_DEVICE_TO_AUTH_TOKEN_CACHE_NAME,
            key = "#authToken.userId + #authToken.device")
    public AuthTokenEntity save(AuthTokenEntity authToken) {
        log.info("Start saving user to repository", authToken);
        final AuthTokenEntity saved;
        try {
            saved = repository.save(authToken);
        } catch (Exception e) {
            log.error("Failed saving auth token to repository", null, authToken);
            throw new ResourceSavingException("Auth token has not saved", authToken, e);
        }
        log.info("Auth token entity saved", authToken);
        log.info("Auth token entity cache updated", authToken);
        return saved;
    }

    @Override
    @Transactional
    @CachePut(value = AuthTokenCacheNames.USER_ID_AND_DEVICE_TO_AUTH_TOKEN_CACHE_NAME,
            key = "#authToken.userId + #authToken.device")
    public AuthTokenEntity update(AuthTokenEntity authToken) {
        log.info("Updating auth token entity", authToken);
        final AuthTokenEntity updated;
        try {
            updated = repository.save(authToken);
        } catch (Exception e) {
            log.error("Failed updating auth token in repository", null, authToken);
            throw new ResourceUpdatingException("Auth token has not updated", e, authToken);
        }
        log.info("Auth token entity updated", updated);
        log.info("Auth token entity cache updated", updated);
        return updated;
    }

    @Override
    @Transactional
    @CachePut(value = AuthTokenCacheNames.USER_ID_AND_DEVICE_TO_AUTH_TOKEN_CACHE_NAME,
            key = "#authTokenEntityDetails.userId + #authTokenEntityDetails.device")
    public AuthTokenEntity delete(AuthTokenEntityDetails authTokenEntityDetails) {
        log.info("Start deleting auth token by username from repository", authTokenEntityDetails);
        final AuthTokenEntity authToken;
        try {
            authToken = repository.getByDetails(authTokenEntityDetails);
            repository.deleteByDetails(authTokenEntityDetails);
        } catch (Exception e) {
            log.error("Failed deleting auth token by details from repository", null, authTokenEntityDetails);
            throw new ResourceDeletionException("The auth token was not deleted", e, authTokenEntityDetails);
        }
        log.info("Auth token deleted from repository by details", authToken);
        log.info("Deleted auth token cache", authToken);
        return null;
    }
}
