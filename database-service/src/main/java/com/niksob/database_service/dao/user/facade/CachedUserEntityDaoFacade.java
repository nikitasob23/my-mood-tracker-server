package com.niksob.database_service.dao.user.facade;

import com.niksob.database_service.dao.user.existence.UserEntityExistenceDao;
import com.niksob.database_service.dao.user.loader.UserEntityLoaderDao;
import com.niksob.database_service.dao.user.updater.UserEntityUpdaterDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.handler.exception.UserDaoExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CachedUserEntityDaoFacade implements UserEntityDaoFacade {
    private final UserEntityLoaderDao loaderDao;
    @Qualifier("userEntityExistenceDao")
    private final UserEntityExistenceDao existenceDao;
    private final UserEntityUpdaterDao updaterDao;

    private final UserDaoExceptionHandler exceptionHandler;

    @Override
    public boolean existsByUsername(String username) {
        return existenceDao.existsByUsername(username);
    }

    @Override
    public boolean existsById(Long id) {
        return existenceDao.existsById(id);
    }

    @Override
    public UserEntity loadById(Long id) {
        final UserEntity user = loaderDao.loadById(id);
        if (user == null) {
            throw exceptionHandler.createResourceNotFoundException(id);
        }
        return user;
    }

    @Override
    public UserEntity loadByUsername(String username) {
        final UserEntity user = loaderDao.loadByUsername(username);
        if (user == null) {
            throw exceptionHandler.createResourceNotFoundException(username);
        }
        return user;
    }

    @Override
    public UserEntity save(UserEntity user) {
        if (existsByUsername(user.getUsername())) {
            throw exceptionHandler.createResourceAlreadyExistsException(user);
        }
        return updaterDao.save(user);
    }

    @Override
    public UserEntity update(UserEntity user) {
        if (!existsByUsername(user.getUsername())) {
            throw exceptionHandler.createResourceNotFoundException(user.getUsername());
        }
        return updaterDao.update(user);
    }

    @Override
    public void delete(String username) {
        if (!existsByUsername(username)) {
            throw exceptionHandler.createResourceNotFoundException(username);
        }
        updaterDao.delete(username);
    }
}
