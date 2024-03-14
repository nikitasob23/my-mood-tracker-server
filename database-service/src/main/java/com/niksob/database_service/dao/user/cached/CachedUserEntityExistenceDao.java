package com.niksob.database_service.dao.user.cached;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.database_service.dao.user.cached.loader.CachedUserEntityLoaderDao;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;

public class CachedUserEntityExistenceDao extends CachedUserEntityLoaderDao implements UserEntityExistenceDao, CacheCleaner {
    public static final String USER_EXISTENCE_CACHE_NAME = "user_existence";
    private final Cache userExistenceCache;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityExistenceDao.class);

    public CachedUserEntityExistenceDao(UserRepository userRepository, Cache userExistenceCache) {
        super(userRepository);
        this.userExistenceCache = userExistenceCache;
    }

    @Override
    @Cacheable(value = USER_EXISTENCE_CACHE_NAME, key = "#userId")
    public boolean exists(Long userId) {
        try {
            super.loadById(userId);
        } catch (ResourceNotFoundException e) {
            log.debug("Value added to cache: user not exists", null, userId);
            return false;
        }
        log.debug("Value added to cache: user exists", null, userId);
        return true;
    }

    public void putUserExistenceCache(Long userId) {
        userExistenceCache.put(userId, true);
        log.debug("Value added to cache: user exists", null, userId);
    }

    public void deleteUserExistenceCache(Long userId) {
        userExistenceCache.put(userId, false);
        log.debug("Value added to cache: user not exists", null, userId);
    }

    @Override
    public void clearCache() {
        log.debug("User existence cache was clear", null);
        userExistenceCache.clear();
    }
}
