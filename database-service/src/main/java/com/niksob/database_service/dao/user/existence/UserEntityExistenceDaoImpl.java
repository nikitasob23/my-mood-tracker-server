package com.niksob.database_service.dao.user.existence;

import com.niksob.database_service.dao.user.loader.UserEntityLoaderDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserEntityExistenceDaoImpl implements UserEntityExistenceDao {
    private final UserEntityLoaderDao loaderDao;

    @Override
    public boolean existsByEmail(String email) {
        return loaderDao.loadByEmail(email) != null;
    }

    @Override
    public boolean existsByUsername(String username) {
        return loaderDao.loadByUsername(username) != null;
    }

    @Override
    public boolean existsById(Long id) {
        return loaderDao.loadById(id) != null;
    }
}
