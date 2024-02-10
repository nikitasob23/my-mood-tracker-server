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

import java.util.Objects;
import java.util.stream.Stream;

@Configuration
public class UserDaoConfig {

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserDaoConfig.class);

    @Bean
    public UserEntityDao getCachedUserDao(UserRepository userRepository, CacheManager cacheManager) {
        Cache userEntityCache = Stream.of(CachedUserEntityDao.USER_CACHE_ENTITY_NAME)
                .map(cacheManager::getCache)
                .filter(Objects::nonNull)
                .findFirst().orElseThrow(this::createCacheStorageNotFoundException);
        return new CachedUserEntityDao(userRepository, userEntityCache);
    }

    private IllegalStateException createCacheStorageNotFoundException() {
        final IllegalStateException e = new IllegalStateException("User entity cache storage not found");
        log.error("UserDao instance was not created by cache key", e, CachedUserEntityDao.USER_CACHE_ENTITY_NAME);
        return e;
    }
}