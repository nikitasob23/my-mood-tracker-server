package com.niksob.database_service.dao.user.updater;

import com.niksob.database_service.dao.user.values.UserCacheNames;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.domain.exception.resource.*;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class CachedUserEntityUpdaterDao implements UserEntityUpdaterDao {
    private final UserRepository userRepository;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserCacheNames.class);

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = UserCacheNames.USER_BY_USERNAME_CACHE_NAME, key = "#userEntity.username"),
            @CachePut(value = UserCacheNames.USER_BY_ID_CACHE_NAME, key = "#userEntity.id")
    })
    public UserEntity save(UserEntity userEntity) {
        log.info("Start saving user to repository", userEntity);
        try {
            final UserEntity saved = userRepository.save(userEntity);
            log.info("User entity saved", userEntity);
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
            @CachePut(value = UserCacheNames.USER_BY_USERNAME_CACHE_NAME, key = "#userEntity.username"),
            @CachePut(value = UserCacheNames.USER_BY_ID_CACHE_NAME, key = "#userEntity.id")
    })
    public UserEntity update(UserEntity userEntity) {
        log.info("Updating user entity", userEntity);
        final UserEntity updated;
        try {
            updated = userRepository.save(userEntity);
            userRepository.flush(); // For immediate invocation of save() and throwing an exception in the try-catch block
        } catch (Exception e) {
            log.error("Failed updating user in repository", null, userEntity);
            throw new ResourceUpdatingException("User has not updated", e, userEntity.getId());
        }
        log.info("User entity updated", updated);
        log.info("User entity cache updated", updated);
        return updated;
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = UserCacheNames.USER_BY_USERNAME_CACHE_NAME, key = "#user.username"),
            @CachePut(value = UserCacheNames.USER_BY_ID_CACHE_NAME, key = "#user.id")
    })
    public UserEntity delete(UserEntity user) {
        log.info("Start deleting user by user from repository", user);
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            log.error("Failed deleting user by user from repository", null, user);
            throw new ResourceDeletionException("The user was not deleted", e, user);
        }
        log.info("User deleted from repository", user);
        log.info("Deleted user cache", user);
        return null;
    }
}