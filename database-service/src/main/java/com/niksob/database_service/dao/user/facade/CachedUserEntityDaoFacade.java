package com.niksob.database_service.dao.user.facade;

import com.niksob.database_service.dao.auth.token.facade.AuthTokenEntityFacadeDao;
import com.niksob.database_service.dao.mood.entry.MoodEntryEntityDao;
import com.niksob.database_service.dao.mood.tag.facade.TagEntityDaoFacade;
import com.niksob.database_service.dao.user.existence.UserEntityExistenceDao;
import com.niksob.database_service.dao.user.loader.UserEntityLoaderDao;
import com.niksob.database_service.dao.user.updater.UserEntityUpdaterDao;
import com.niksob.database_service.entity.user.UserEntity;
import com.niksob.database_service.handler.exception.DaoExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class CachedUserEntityDaoFacade implements UserEntityDaoFacade {
    private final UserEntityLoaderDao loaderDao;
    private final UserEntityExistenceDao existenceDao;
    private final UserEntityUpdaterDao updaterDao;

    private final TagEntityDaoFacade tagEntityDaoFacade;
    private final MoodEntryEntityDao moodEntryEntityDao;
    private final AuthTokenEntityFacadeDao authTokenEntityFacadeDao;

    @Qualifier("userDaoExceptionHandler")
    private final DaoExceptionHandler exceptionHandler;

    @Override
    public boolean existsByEmail(String email) {
        return existenceDao.existsByEmail(email);
    }

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
        if (!existsById(user.getId())) {
            throw exceptionHandler.createResourceNotFoundException(user.getEmail());
        }
        return updaterDao.update(user);
    }

    @Override
    @Transactional
    public void delete(String username) {
        final UserEntity user = loaderDao.loadByUsername(username);
        if (user == null) {
            throw exceptionHandler.createResourceNotFoundException(username);
        }
        moodEntryEntityDao.deleteAllByUserId(user.getId());
        tagEntityDaoFacade.deleteAllByUserId(user.getId());
        authTokenEntityFacadeDao.deleteByUserId(user.getId());
        updaterDao.delete(user);
    }
}
