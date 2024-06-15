package com.niksob.database_service.dao.auth.token.loader;

import com.niksob.database_service.dao.auth.token.values.AuthTokenCacheNames;
import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.handler.exception.DaoExceptionHandler;
import com.niksob.database_service.repository.auth.token.AuthTokenRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
@AllArgsConstructor
public class CachedAuthTokenEntityLoaderDao implements AuthTokenEntityLoaderDao {
    private final AuthTokenRepository authTokenRepository;

    @Qualifier("authTokenDaoExceptionHandler")
    private final DaoExceptionHandler exceptionHandler;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedAuthTokenEntityLoaderDao.class);

    @Override
    @Cacheable(value = AuthTokenCacheNames.USER_ID_AND_DEVICE_TO_AUTH_TOKEN_CACHE_NAME, key = "#userId")
    public Set<AuthTokenEntity> loadAllByUserId(Long userId) {
        log.info("Start loading all user's auth tokens from repository", userId);
        final Set<AuthTokenEntity> authTokens;
        try {
            authTokens = authTokenRepository.getAllByUserId(userId);
        } catch (Exception e) {
            throw exceptionHandler.createResourceNotFoundException(userId, e);
        }
        log.info("All user's auth tokens loaded from repository",
                Objects.requireNonNullElse(authTokens, ""));
        return authTokens;
    }
}
