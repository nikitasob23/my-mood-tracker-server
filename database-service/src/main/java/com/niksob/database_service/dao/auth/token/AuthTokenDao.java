package com.niksob.database_service.dao.auth.token;

import com.niksob.database_service.mapper.entity.auth.token.encoded.EncodedAuthTokenEntityMapper;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.layer_connector.annotation.LayerConnector;

@LayerConnector(source = AuthTokenEntityDao.class, mapper = EncodedAuthTokenEntityMapper.class)
public interface AuthTokenDao {
    EncodedAuthToken save(EncodedAuthToken authToken);
}
