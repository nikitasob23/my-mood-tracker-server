package com.niksob.database_service.dao.auth.token;

import com.niksob.database_service.dao.auth.token.facade.AuthTokenEntityFacadeDao;
import com.niksob.database_service.mapper.entity.auth.token.details.AuthTokenEntityDetailsMapper;
import com.niksob.database_service.mapper.entity.auth.token.encoded.EncodedAuthTokenEntityMapper;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.layer_connector.annotation.LayerConnector;

@LayerConnector(source = AuthTokenEntityFacadeDao.class, mapper = {
        EncodedAuthTokenEntityMapper.class, AuthTokenEntityDetailsMapper.class
})
public interface AuthTokenDao {
    EncodedAuthToken load(AuthTokenDetails authTokenDetails);

    EncodedAuthToken save(EncodedAuthToken authToken);

    void update(EncodedAuthToken authToken);

    void delete(AuthTokenDetails authTokenDetails);
}
