package com.niksob.database_service.dao.user;

import com.niksob.database_service.cache.cleaner.CacheCleaner;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.exception.entity.EntityNotDeletedException;
import com.niksob.database_service.exception.entity.EntitySavingException;
import com.niksob.database_service.exception.entity.EntityUpdatingException;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Stream;

@AllArgsConstructor
public class CachedUserEntityDao implements UserEntityDao, CacheCleaner {
    public static final String USER_CACHE_ENTITY_NAME = "users";
    private final UserRepository userRepository;
    private final Cache cache;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityDao.class);

    @Override
    @Cacheable(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#username")
    public UserEntity load(String username) {
        log.debug("Start loading user entity by username from repository", username);
        return Stream.of(username)
                .map(userRepository::getByUsername)
                .filter(Objects::nonNull)
                .peek(userEntity -> log.debug("User entity loaded from repository", userEntity))
                .peek(userEntity -> log.debug("Cached user entity", userEntity))
                .findFirst().orElseThrow(() -> createEntityNotFoundException(username));
    }

    @Override
    @CachePut(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#userEntity.username")
    public UserEntity save(UserEntity userEntity) {
        log.debug("Saving user info", userEntity);
        try {
            return Stream.of(userEntity)
                    .peek(userRepository::save)
                    .peek(u -> log.debug("User entity saved", u))
                    .peek(u -> log.debug("User entity cache updated", u))
                    .findFirst().get();
        } catch (Exception e) {
            final EntitySavingException entitySavingException = new EntitySavingException(userEntity.getUsername(), e);
            log.error("User entity has not been saved", e, userEntity);
            throw entitySavingException;
        }
    }

    @Override
    @CachePut(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#userEntity.username")
    public UserEntity update(UserEntity userEntity) {
        log.debug("Updating user entity", userEntity);
        try {
            return Stream.of(userEntity)
                    .peek(userRepository::save)
                    .peek(u -> log.debug("User entity updated", u))
                    .peek(u -> log.debug("User entity cache updated", u))
                    .findFirst().get();
        } catch (Exception e) {
            final EntityUpdatingException entityUpdatingException =
                    new EntityUpdatingException(userEntity.getUsername(), e);
            log.error("User entity has not been updated", e, userEntity);
            throw entityUpdatingException;
        }
    }

    @Override
    @Transactional
    public void delete(String username) {
        log.debug("Start deleting user entity by username from repository", username);
        try {
            Stream.of(username)
                    .peek(cache::evict)
                    .peek(userRepository::deleteUserEntityByUsername)
                    .peek(u -> log.debug("User entity deleted from repository", u))
                    .forEach(u -> log.debug("Deleted user entity cache", u));
        } catch (Exception e) {
            final EntityNotDeletedException entityNotDeletedException =
                    new EntityNotDeletedException("User entity not delete by username", username);
            log.error("Failed deleting user by username from repository", e, username);
            throw entityNotDeletedException;
        }
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    private EntityNotFoundException createEntityNotFoundException(String username) {
        final EntityNotFoundException e = new EntityNotFoundException("Username not found by username");
        log.error("Failed loading user by username from repository", e, username);
        return e;
    }
}