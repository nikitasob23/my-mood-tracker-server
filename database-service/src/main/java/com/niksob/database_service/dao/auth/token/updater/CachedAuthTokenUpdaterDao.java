package com.niksob.database_service.dao.auth.token.updater;

import com.niksob.database_service.dao.auth.token.values.AuthTokenCacheNames;
import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.repository.auth.token.AuthTokenRepository;
import com.niksob.domain.exception.resource.ResourceDeletionException;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.exception.resource.ResourceUpdatingException;
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
            key = "#authToken.getId() + #authToken.getDevice()")
    public AuthTokenEntity delete(AuthTokenEntity authToken) {
        log.info("Start deleting auth token from repository", authToken);
        try {
            repository.delete(authToken);
        } catch (Exception e) {
            log.error("Failed deleting auth token from repository", null, authToken);
            throw new ResourceDeletionException("The auth token was not deleted", e, authToken);
        }
        log.info("Auth token deleted from repository", authToken);
        log.info("Deleted auth token cache", authToken);
        return null;
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        log.info("Start deleting all user's auth tokens from repository", userId);
        try {
            repository.deleteAllByUserId(userId);
        } catch (Exception e) {
            log.error("Failed deleting all user's auth tokens from repository", null, userId);
            throw new ResourceDeletionException("All user's auth tokens were not deleted", e, userId);
        }
        log.info("Successful deletion all user's auth tokens from repository", userId);
    }
}
