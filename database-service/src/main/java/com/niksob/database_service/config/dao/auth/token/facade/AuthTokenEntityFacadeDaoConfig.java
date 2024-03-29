package com.niksob.database_service.config.dao.auth.token.facade;

import com.niksob.database_service.dao.auth.token.existence.AuthTokenEntityExistenceDao;
import com.niksob.database_service.dao.auth.token.facade.AuthTokenEntityFacadeDao;
import com.niksob.database_service.dao.auth.token.facade.CachedAuthTokenFacadeDaoFacade;
import com.niksob.database_service.dao.auth.token.loader.AuthTokenEntityLoaderDao;
import com.niksob.database_service.dao.auth.token.updater.AuthTokenEntityUpdaterDao;
import com.niksob.database_service.dao.auth.token.values.AuthTokenCacheNames;
import com.niksob.database_service.handler.exception.DaoExceptionHandler;
import com.niksob.database_service.mapper.entity.auth.token.details.AuthTokenEntityDetailsMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class AuthTokenEntityFacadeDaoConfig {
    private final CacheManager cacheManager;

    @Bean
    public AuthTokenEntityFacadeDao getCachedAuthTokenUpdaterDao(
            AuthTokenEntityLoaderDao loaderDao,
            AuthTokenEntityExistenceDao existenceDao,
            AuthTokenEntityUpdaterDao updaterDao,
            AuthTokenEntityDetailsMapper authTokenDetailsMapper,
            @Qualifier("authTokenDaoExceptionHandler") DaoExceptionHandler daoExceptionHandler
    ) {
        final Cache cache = cacheManager.getCache(AuthTokenCacheNames.USER_ID_AND_DEVICE_TO_AUTH_TOKEN_CACHE_NAME);
        return new CachedAuthTokenFacadeDaoFacade(
                loaderDao,
                existenceDao,
                updaterDao,
                cache,
                authTokenDetailsMapper,
                daoExceptionHandler
        );
    }
}
