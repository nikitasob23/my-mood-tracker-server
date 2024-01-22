package com.niksob.database_service.dao.user.cached.update;

import com.niksob.database_service.dao.user.cached.CachedUserEntityDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.exception.entity.EntityUpdatingException;
import com.niksob.database_service.repository.user.UserRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;

import java.util.stream.Stream;

public class UpdatableUserEntityDao extends CachedUserEntityDao {

    public UpdatableUserEntityDao(UserRepository userRepository, Cache cache) {
        super(cache, userRepository);
    }

    @Override
    @CachePut(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#userEntity.username")
    public UserEntity update(UserEntity userEntity) {
        inflateEntityFromRepository(userEntity);

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

    private void inflateEntityFromRepository(UserEntity userEntity) {
        final UserEntity loaded = super.load(userEntity.getUsername());
        userEntity.setId(loaded.getId());
    }
}
