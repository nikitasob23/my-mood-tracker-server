package com.niksob.database_service.dao.auth.token;

import com.niksob.database_service.dao.auth.token.values.AuthTokenCacheNames;
import com.niksob.database_service.entity.user.token.refresh.AuthTokenEntity;
import com.niksob.database_service.repository.auth.token.AuthTokenRepository;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class CacheableAuthTokenEntityDao implements AuthTokenEntityDao {
    private final AuthTokenRepository authTokenRepository;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CacheableAuthTokenEntityDao.class);

    @Override
    @Transactional
    @CachePut(value = AuthTokenCacheNames.USER_ID_TO_AUTH_TOKEN_CACHE_NAME, key = "#authToken.userId")
    public AuthTokenEntity save(AuthTokenEntity authToken) {
        log.info("Start saving user to repository", authToken);
        final AuthTokenEntity saved;
        try {
            saved = authTokenRepository.save(authToken);
        } catch (Exception e) {
            log.error("Failed saving auth token to repository", null, authToken);
            throw new ResourceSavingException("Auth token has not saved", authToken, e);
        }
        log.info("Auth token entity saved", authToken);
        log.info("Auth token entity cache updated", authToken);
        return saved;
    }
}
