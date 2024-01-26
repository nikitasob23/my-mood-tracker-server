package com.niksob.database_service.dao.user.cached.update;

import com.niksob.database_service.dao.user.cached.CachedUserEntityDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.exception.entity.EntityUpdatingException;
import com.niksob.database_service.repository.user.UserRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;

public class UpdatableUserEntityDao extends CachedUserEntityDao {

    public UpdatableUserEntityDao(UserRepository userRepository, Cache cache) {
        super(cache, userRepository);
    }

    @Override
    @Transactional
    @CachePut(value = CachedUserEntityDao.USER_CACHE_ENTITY_NAME, key = "#userEntity.username")
    public UserEntity update(UserEntity userEntity) {
        log.debug("Updating user entity", userEntity);
        try {
            addDbReferences(userEntity);
            final UserEntity saved = userRepository.save(userEntity);
            log.debug("User entity updated", userEntity);
            log.debug("User entity cache updated", userEntity);
            return saved;
        } catch (Exception e) {
            final EntityUpdatingException entityUpdatingException =
                    new EntityUpdatingException(userEntity.getUsername(), e);
            log.error("User entity has not been updated", e, userEntity);
            throw entityUpdatingException;
        }
    }
}
