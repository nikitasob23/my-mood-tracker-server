package com.niksob.database_service.dao.user;

import com.niksob.database_service.dao.user.existence.UserEntityExistenceDao;
import com.niksob.database_service.dao.user.facade.UserEntityDaoFacade;
import com.niksob.database_service.mapper.entity.user.UserEntityMapper;
import com.niksob.database_service.mapper.entity.user.id.UserIdEntityMapper;
import com.niksob.domain.model.user.UserId;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;
import com.niksob.layer_connector.annotation.LayerConnector;

@LayerConnector(
        source = UserEntityDaoFacade.class,
        sourceParents = UserEntityExistenceDao.class,
        mapper = {UserEntityMapper.class, UserIdEntityMapper.class}
)
public interface UserDao {
    boolean existsByUsername(Username username);

    boolean existsById(UserId id);

    UserInfo loadById(UserId userId);

    UserInfo loadByUsername(Username username);

    UserInfo save(UserInfo userInfo);

    UserInfo update(UserInfo userInfo);

    void delete(Username username);
}
