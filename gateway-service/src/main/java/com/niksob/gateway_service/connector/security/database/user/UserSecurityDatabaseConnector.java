package com.niksob.gateway_service.connector.security.database.user;

import com.niksob.domain.http.connector.microservice.database.user.UserDatabaseConnector;
import com.niksob.domain.mapper.user.UsernameMapper;
import com.niksob.gateway_service.mapper.user.UserSecurityDetailsMonoMapper;
import com.niksob.gateway_service.model.user.security.UserSecurityDetails;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = UserDatabaseConnector.class, mapper = {
        UsernameMapper.class, UserSecurityDetailsMonoMapper.class
})
public interface UserSecurityDatabaseConnector {
    Mono<UserSecurityDetails> load(String username);
}
