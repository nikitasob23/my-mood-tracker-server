package com.niksob.database_service.dao.auth.token.loader;

import com.niksob.database_service.dao.auth.token.values.AuthTokenCacheNames;
import com.niksob.database_service.entity.auth.token.AuthTokenEntity;
import com.niksob.database_service.handler.exception.DaoExceptionHandler;
import com.niksob.database_service.repository.auth.token.AuthTokenRepository;
import com.niksob.database_service.model.auth.token.details.AuthTokenEntityDetails;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class CachedAuthTokenEntityLoaderDao implements AuthTokenEntityLoaderDao {
    private final AuthTokenRepository authTokenRepository;

    @Qualifier("authTokenDaoExceptionHandler")
    private final DaoExceptionHandler exceptionHandler;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedAuthTokenEntityLoaderDao.class);

    @Override
    @Cacheable(value = AuthTokenCacheNames.USER_ID_AND_DEVICE_TO_AUTH_TOKEN_CACHE_NAME,
            key = "#authTokenEntityDetails.userId + #authTokenEntityDetails.device")
    public AuthTokenEntity load(AuthTokenEntityDetails authTokenEntityDetails) {
        log.info("Start loading auth token entity by details from repository", authTokenEntityDetails);
        final AuthTokenEntity authToken;
        try {
            authToken = authTokenRepository.getByDetails(authTokenEntityDetails);
        } catch (Exception e) {
            throw exceptionHandler.createResourceNotFoundException(authTokenEntityDetails, e);
        }
        log.info("Auth token entity loaded from repository", Objects.requireNonNullElse(authToken, ""));
        log.info("Cached auth token entity", Objects.requireNonNullElse(authToken, ""));
        return authToken;
    }
}
