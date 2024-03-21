package com.niksob.domain.http.connector.auth.token;

import com.niksob.domain.http.connector.auth.token.dto.AuthTokenDatabaseDtoConnector;
import com.niksob.domain.mapper.dto.auth.token.encoded.EncodedAuthTokenDtoMapper;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenDatabaseDtoConnector.class, mapper = EncodedAuthTokenDtoMapper.class)
public interface AuthTokenDatabaseConnector {
    Mono<Void> save(EncodedAuthToken authToken);
}
