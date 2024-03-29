package com.niksob.database_service.dao.auth.token;

import com.niksob.database_service.dao.auth.token.facade.AuthTokenEntityFacadeDao;
import com.niksob.database_service.mapper.entity.auth.token.details.AuthTokenEntityDetailsMapper;
import com.niksob.database_service.mapper.entity.auth.token.encoded.EncodedAuthTokenEntityMapper;
import com.niksob.database_service.mapper.entity.user.id.UserIdEntityMapper;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.domain.model.user.UserId;
import com.niksob.layer_connector.annotation.LayerConnector;

@LayerConnector(source = AuthTokenEntityFacadeDao.class, mapper = {
        EncodedAuthTokenEntityMapper.class, AuthTokenEntityDetailsMapper.class, UserIdEntityMapper.class
})
public interface AuthTokenDao {
    boolean existsByDetails(AuthTokenDetails authTokenDetails);

    EncodedAuthToken load(AuthTokenDetails authTokenDetails);

    EncodedAuthToken save(EncodedAuthToken authToken);

    EncodedAuthToken update(EncodedAuthToken authToken);

    void delete(AuthTokenDetails authTokenDetails);

    void deleteByUserId(UserId userId);
}
