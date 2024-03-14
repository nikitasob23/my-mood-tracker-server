package com.niksob.database_service.dao.user.cached;

import com.niksob.database_service.dao.user.UserEntityDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.domain.exception.resource.*;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;

public class CachedUserEntityDao extends CachedUserEntityExistenceDao implements UserEntityDao {
    public static final String USER_BY_USERNAME_CACHE_NAME = "username_user";
    public static final String USER_BY_ID_CACHE_NAME = "id_user";

    private final Cache usernameToUserCache;
    private final Cache idToUserCache;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityDao.class);

    public CachedUserEntityDao(
            UserRepository userRepository,
            Cache userExistenceCache,
            Cache usernameToUserCache,
            Cache idToUserCache
    ) {
        super(userRepository, userExistenceCache);
        this.usernameToUserCache = usernameToUserCache;
        this.idToUserCache = idToUserCache;
    }

    @Override
    @Transactional
    public UserEntity loadById(Long id) {
        final UserEntity userEntity = super.loadById(id);
        super.putUserExistenceCache(userEntity.getId());
        return userEntity;
    }

    @Override
    @Transactional
    public UserEntity loadByUsername(String username) {
        final UserEntity userEntity = super.loadByUsername(username);
        super.putUserExistenceCache(userEntity.getId());
        return userEntity;
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = CachedUserEntityDao.USER_BY_USERNAME_CACHE_NAME, key = "#userEntity.username"),
            @CachePut(value = CachedUserEntityDao.USER_BY_ID_CACHE_NAME, key = "#userEntity.id")
    })
    public UserEntity save(UserEntity userEntity) {
        log.info("Start saving user to repository", userEntity);

        if (userRepository.existsByUsername(userEntity.getUsername())) {
            log.error("Failed saving user to repository", null, userEntity);
            throw new ResourceAlreadyExistsException("User already exists", null, userEntity.getUsername());
        }
        try {
            final UserEntity saved = userRepository.save(userEntity);
            log.info("User entity saved", userEntity);

            super.putUserExistenceCache(userEntity.getId());
            log.info("User entity cache updated", userEntity);
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
        log.info("Updating user entity", userEntity);
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
        log.info("User entity updated", updated);
        super.putUserExistenceCache(userEntity.getId());
        log.info("User entity cache updated", updated);
        return updated;
    }

    @Override
    @Transactional
    public void delete(String username) {
        log.info("Start deleting user by username from repository", username);
        if (!userRepository.existsByUsername(username)) {
            throw createResourceNotFoundException(username);
        }
        try {
            final UserEntity user = super.loadByUsername(username);
            usernameToUserCache.evict(username);
            idToUserCache.evict(user.getId());
            super.deleteUserExistenceCache(user.getId());

            userRepository.deleteByUsername(username);
            log.info("User deleted from repository", username);
            log.info("Deleted user cache", username);
        } catch (Exception e) {
            log.error("Failed deleting user by username from repository", null, username);
            throw new ResourceDeletionException("The user was not deleted", e, username);
        }
    }

    @Override
    public void clearCache() {
        super.clearCache();
        usernameToUserCache.clear();
        idToUserCache.clear();
    }
}