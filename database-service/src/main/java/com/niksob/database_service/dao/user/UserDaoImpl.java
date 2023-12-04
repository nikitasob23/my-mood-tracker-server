package com.niksob.database_service.dao.user;

import com.niksob.database_service.cache.cleaner.BaseCacheCleaner;
import com.niksob.database_service.exception.entity.EntitySavingException;
import com.niksob.database_service.exception.entity.EntityUpdatingException;
import com.niksob.database_service.mapper.dao.user.UserEntityMapper;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class UserDaoImpl extends BaseCacheCleaner implements UserDao {
    private static final String USER_CACHE_ENTITY_NAME = "users";

    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    private final CacheManager cacheManager;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    @Cacheable(value = UserDaoImpl.USER_CACHE_ENTITY_NAME, key = "#username.value")
    public UserInfo load(Username username) {
        log.debug("Start loading user info by username from repository", username);
        return Stream.of(username)
                .map(userEntityMapper::toEntityUsername)
                .map(userRepository::getByUsername)
                .map(userEntityMapper::fromEntity)
                .filter(Objects::nonNull)
                .peek(userInfo -> log.debug("User info loaded from repository", userInfo))
                .peek(userInfo -> log.debug("Cached user info", userInfo))
                .findFirst().orElseThrow(() -> createEntityNotFoundException(username));
    }

    @Override
    @CachePut(value = UserDaoImpl.USER_CACHE_ENTITY_NAME, key = "#userInfo.username.value")
    public UserInfo save(UserInfo userInfo) {
        log.debug("Saving user info", userInfo);
        try {
            return Stream.of(userInfo)
                    .map(userEntityMapper::toEntity)
                    .peek(userRepository::save)
                    .map(userEntityMapper::fromEntity)
                    .peek(userEntity -> log.debug("User info saved", userEntity))
                    .peek(userEntity -> log.debug("User info cache updated", userEntity))
                    .findFirst().get();
        } catch (Exception e) {
            final EntitySavingException entitySavingException = new EntitySavingException(userInfo.getUsername(), e);
            log.error("User info has not been saved", e, userInfo);
            throw entitySavingException;
        }
    }

    @Override
    @CachePut(value = UserDaoImpl.USER_CACHE_ENTITY_NAME, key = "#userInfo.username.value")
    public UserInfo update(UserInfo userInfo) {
        log.debug("Updating user info", userInfo);
        try {
            return Stream.of(userInfo)
                    .map(userEntityMapper::toEntity)
                    .peek(userRepository::save)
                    .map(userEntityMapper::fromEntity)
                    .peek(userEntity -> log.debug("User info updated", userEntity))
                    .peek(userEntity -> log.debug("User info cache updated", userEntity))
                    .findFirst().get();
        } catch (Exception e) {
            final EntityUpdatingException entityUpdatingException =
                    new EntityUpdatingException(userInfo.getUsername(), e);
            log.error("User info has not been updated", e, userInfo);
            throw entityUpdatingException;
        }
    }

    private EntityNotFoundException createEntityNotFoundException(Username username) {
        final EntityNotFoundException e = new EntityNotFoundException("Username not found by username");
        log.error("Failed loading user by username from repository", e, username);
        return e;
    }

    @Override
    protected CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    protected String getCacheEntityName() {
        return USER_CACHE_ENTITY_NAME;
    }
}