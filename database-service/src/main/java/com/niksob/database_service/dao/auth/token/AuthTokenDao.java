package com.niksob.database_service.dao.auth.token;

import com.niksob.database_service.mapper.entity.auth.token.AuthTokenEntityMapper;
import com.niksob.domain.model.auth.token.UserAuthToken;
import com.niksob.layer_connector.annotation.LayerConnector;

@LayerConnector(source = AuthTokenEntityDao.class, mapper = AuthTokenEntityMapper.class)
public interface AuthTokenDao {
    UserAuthToken save(UserAuthToken authToken);
}
