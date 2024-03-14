package com.niksob.database_service.dao.user.cached.loader;

import com.niksob.database_service.dao.user.cached.CachedUserEntityDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import java.util.Optional;

@AllArgsConstructor
public class CachedUserEntityLoaderDao implements UserEntityLoaderDao {
    protected final UserRepository userRepository;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityLoaderDao.class);

    @Override
    @Caching(cacheable = {
            @Cacheable(value = CachedUserEntityDao.USER_BY_ID_CACHE_NAME, key = "#id"),
            @Cacheable(value = CachedUserEntityDao.USER_BY_USERNAME_CACHE_NAME, key = "#result.username",
                    condition = "#result != null && #result.username != null")
    })
    public UserEntity loadById(Long id) {
        log.info("Start loading user entity by id from repository", id);

        final Optional<UserEntity> userOptional;
        try {
            userOptional = userRepository.findById(id);
        } catch (Exception e) {
            throw createResourceNotFoundException(id, e);
        }
        final UserEntity userEntity = userOptional.orElseThrow(() -> createResourceNotFoundException(id));
        log.info("User entity loaded from repository", userEntity);
        log.info("Cached user entity", userEntity);
        return userEntity;
    }

    @Override
    @Caching(cacheable = {
            @Cacheable(value = CachedUserEntityDao.USER_BY_USERNAME_CACHE_NAME, key = "#username",
                    unless = "#result == null || #result.username == null"),
            @Cacheable(value = CachedUserEntityDao.USER_BY_ID_CACHE_NAME, key = "#result.id",
                    condition = "#result != null && #result.id != null")
    })
    public UserEntity loadByUsername(String username) {
        log.info("Start loading user entity by username from repository", username);

        final UserEntity userEntity;
        try {
            userEntity = userRepository.getByUsername(username);
        } catch (Exception e) {
            throw createResourceNotFoundException(username, e);
        }
        if (userEntity == null) {
            throw createResourceNotFoundException(username);
        }
        log.info("User entity loaded from repository", userEntity);
        log.info("Cached user entity", userEntity);
        return userEntity;
    }

    protected ResourceNotFoundException createResourceNotFoundException(Object state) {
        return createResourceNotFoundException(state, null);
    }

    protected ResourceNotFoundException createResourceNotFoundException(Object state, Exception e) {
        log.error("Failed getting user by username from repository", null, state);
        return new ResourceNotFoundException("The user was not found", e, state);
    }
}
