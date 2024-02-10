package com.niksob.database_service.dao.user.cached;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.database_service.dao.user.UserEntityDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.exception.resource.*;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class CachedUserEntityDao implements UserEntityDao, CacheCleaner {
    public static final String USER_CACHE_ENTITY_NAME = "users";
    protected final UserRepository userRepository;

    private final Cache cache;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityDao.class);

    @Override
    @Cacheable(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#username")
    public UserEntity load(String username) {
        log.debug("Start loading user entity by username from repository", username);

        final UserEntity userEntity;
        try {
            userEntity = userRepository.getByUsername(username);
        } catch (Exception e) {
            throw createResourceNotFoundException(username, e);
        }
        if (userEntity == null) {
            throw createResourceNotFoundException(username);
        }
        log.debug("User entity loaded from repository", userEntity);
        log.debug("Cached user entity", userEntity);
        return userEntity;
    }

    @Override
    @CachePut(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#userEntity.username")
    public UserEntity save(UserEntity userEntity) {
        log.debug("Start saving user to repository", userEntity);

        if (userRepository.existsByUsername(userEntity.getUsername())) {
            log.error("Failed saving user to repository", null, userEntity);
            throw new ResourceAlreadyExistsException("User already exists", null, userEntity.getUsername());
        }
        try {
            final UserEntity saved = userRepository.save(userEntity);
            log.debug("User entity saved", userEntity);
            log.debug("User entity cache updated", userEntity);
            return saved;
        } catch (Exception e) {
            log.error("Failed saving user to repository", null, userEntity);
            throw new ResourceSavingException("User has not saved", userEntity.getUsername(), e);
        }
    }

    @Override
    @Transactional
    @CachePut(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#userEntity.username")
    public UserEntity update(UserEntity userEntity) {
        log.debug("Updating user entity", userEntity);
        if (!userRepository.existsByUsername(userEntity.getUsername())) {
            throw createResourceNotFoundException(userEntity.getUsername());
        }
        final UserEntity updated;
        try {
            updated = userRepository.save(userEntity);
        } catch (Exception e) {
            log.error("Failed updating user in repository", null, userEntity);
            throw new ResourceUpdatingException("User has not updated", e, userEntity.getId());
        }
        log.debug("User entity updated", updated);
        log.debug("User entity cache updated", updated);
        return updated;
    }

    @Override
    @Transactional
    public void delete(String username) {
        log.debug("Start deleting user by username from repository", username);
        if (!userRepository.existsByUsername(username)) {
            throw createResourceNotFoundException(username);
        }
        try {
            cache.evict(username);
            userRepository.deleteByUsername(username);
            log.debug("User deleted from repository", username);
            log.debug("Deleted user cache", username);
        } catch (Exception e) {
            log.error("Failed deleting user by username from repository", null, username);
            throw new ResourceDeletionException("The user was not deleted", e, username);
        }
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    protected ResourceNotFoundException createResourceNotFoundException(String username) {
        return createResourceNotFoundException(username, null);
    }

    private ResourceNotFoundException createResourceNotFoundException(String username, Exception e) {
        log.error("Failed getting user by username from repository", null, username);
        return new ResourceNotFoundException("The user was not found", e, username);
    }
}