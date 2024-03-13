package com.niksob.database_service.config.dao.user;

import com.niksob.database_service.dao.user.cached.CachedUserEntityDao;
import com.niksob.database_service.dao.user.UserEntityDao;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class UserDaoConfig {

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserDaoConfig.class);

    @Bean
    public UserEntityDao getCachedUserDao(UserRepository userRepository, CacheManager cacheManager) {
        final Cache usernameToUserCache = cacheManager.getCache(CachedUserEntityDao.USER_BY_USERNAME_CACHE_NAME);
        final Cache idToUserCache = cacheManager.getCache(CachedUserEntityDao.USER_BY_ID_CACHE_NAME);
        if (usernameToUserCache == null) {
            throwCacheStorageNotFoundException(CachedUserEntityDao.USER_BY_USERNAME_CACHE_NAME);
        }
        if (idToUserCache == null) {
            throwCacheStorageNotFoundException(CachedUserEntityDao.USER_BY_ID_CACHE_NAME);
        }
        return new CachedUserEntityDao(userRepository, usernameToUserCache, idToUserCache);
    }

    private void throwCacheStorageNotFoundException(String cacheName) {
        final IllegalStateException e = new IllegalStateException("User entity cache storage not found");
        log.error("UserDao instance was not created by cache key", e, cacheName);
        throw e;
    }
}