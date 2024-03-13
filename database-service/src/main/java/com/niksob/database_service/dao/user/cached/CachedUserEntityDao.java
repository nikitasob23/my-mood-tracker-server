package com.niksob.database_service.dao.user.cached;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.database_service.dao.user.UserEntityDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.domain.exception.resource.*;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CachedUserEntityDao implements UserEntityDao, CacheCleaner {
    public static final String USER_BY_USERNAME_CACHE_NAME = "username_user";
    public static final String USER_BY_ID_CACHE_NAME = "id_user";

    protected final UserRepository userRepository;

    private final Cache usernameToUserCache;
    private final Cache idToUserCache;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityDao.class);

    public CachedUserEntityDao(UserRepository userRepository, Cache usernameToUserCache, Cache idToUserCache) {
        this.userRepository = userRepository;
        this.usernameToUserCache = usernameToUserCache;
        this.idToUserCache = idToUserCache;
    }

    @Override
    @Caching(cacheable = {
            @Cacheable(value = CachedUserEntityDao.USER_BY_ID_CACHE_NAME, key = "#id"),
            @Cacheable(value = CachedUserEntityDao.USER_BY_USERNAME_CACHE_NAME, key = "#result.username",
                    condition = "#result != null && #result.username != null")
    })
    public UserEntity loadById(Long id) {
        log.debug("Start loading user entity by id from repository", id);

        final Optional<UserEntity> userOptional;
        try {
            userOptional = userRepository.findById(id);
        } catch (Exception e) {
            throw createResourceNotFoundException(id, e);
        }
        final UserEntity userEntity = userOptional.orElseThrow(() -> createResourceNotFoundException(id));
        log.debug("User entity loaded from repository", userEntity);
        log.debug("Cached user entity", userEntity);
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
    @Caching(put = {
            @CachePut(value = CachedUserEntityDao.USER_BY_USERNAME_CACHE_NAME, key = "#userEntity.username"),
            @CachePut(value = CachedUserEntityDao.USER_BY_ID_CACHE_NAME, key = "#userEntity.id")
    })
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
    @Caching(put = {
            @CachePut(value = CachedUserEntityDao.USER_BY_USERNAME_CACHE_NAME, key = "#userEntity.username"),
            @CachePut(value = CachedUserEntityDao.USER_BY_ID_CACHE_NAME, key = "#userEntity.id")
    })
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
            final UserEntity user = loadByUsername(username);
            usernameToUserCache.evict(username);
            idToUserCache.evict(user.getId());

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
        usernameToUserCache.clear();
        idToUserCache.clear();
    }

    protected ResourceNotFoundException createResourceNotFoundException(Object state) {
        return createResourceNotFoundException(state, null);
    }

    private ResourceNotFoundException createResourceNotFoundException(Object state, Exception e) {
        log.error("Failed getting user by username from repository", null, state);
        return new ResourceNotFoundException("The user was not found", e, state);
    }
}