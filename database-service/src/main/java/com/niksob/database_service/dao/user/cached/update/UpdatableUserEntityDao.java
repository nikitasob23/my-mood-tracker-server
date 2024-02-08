package com.niksob.database_service.dao.user.cached.update;

import com.niksob.database_service.dao.user.cached.CachedUserEntityDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.exception.resource.ResourceUpdatingException;
import com.niksob.database_service.repository.user.UserRepository;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;

public class UpdatableUserEntityDao extends CachedUserEntityDao {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UpdatableUserEntityDao.class);

    public UpdatableUserEntityDao(UserRepository userRepository, Cache cache) {
        super(userRepository, cache);
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
}
