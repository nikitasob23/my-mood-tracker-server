package com.niksob.database_service.dao.user.loader;

import com.niksob.database_service.dao.user.values.UserCacheNames;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.handler.exception.DaoExceptionHandler;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class CachedUserEntityLoaderDao implements UserEntityLoaderDao {
    protected final UserRepository userRepository;

    @Qualifier("userDaoExceptionHandler")
    private final DaoExceptionHandler exceptionHandler;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityLoaderDao.class);

    @Override
    @Caching(cacheable = {
            @Cacheable(value = UserCacheNames.USER_BY_ID_CACHE_NAME, key = "#id"),
            @Cacheable(value = UserCacheNames.USER_BY_USERNAME_CACHE_NAME, key = "#result.username",
                    condition = "#result != null && #result.username != null")
    })
    public UserEntity loadById(Long id) {
        log.info("Start loading user entity by id from repository", id);
        final UserEntity user;
        try {
            user = userRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw exceptionHandler.createResourceNotFoundException(id, e);
        }
        log.info("User entity loaded from repository", Objects.requireNonNullElse(user, ""));
        log.info("Cached user entity", Objects.requireNonNullElse(user, ""));
        return user;
    }

    @Override
    @Caching(cacheable = {
            @Cacheable(value = UserCacheNames.USER_BY_USERNAME_CACHE_NAME, key = "#username"),
            @Cacheable(value = UserCacheNames.USER_BY_ID_CACHE_NAME, key = "#result.id",
                    condition = "#result != null && #result.id != null")
    })
    public UserEntity loadByUsername(String username) {
        log.info("Start loading user entity by username from repository", username);

        final UserEntity user;
        try {
            user = userRepository.getByUsername(username);
        } catch (Exception e) {
            throw exceptionHandler.createResourceNotFoundException(username, e);
        }
        log.info("User entity loaded from repository", Objects.requireNonNullElse(user, ""));
        log.info("Cached user entity", Objects.requireNonNullElse(user, ""));
        return user;
    }
}
