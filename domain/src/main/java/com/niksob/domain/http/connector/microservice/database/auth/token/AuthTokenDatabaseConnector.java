package com.niksob.domain.http.connector.microservice.database.auth.token;

import com.niksob.domain.http.connector.microservice.database.auth.token.dto.AuthTokenDatabaseDtoConnector;
import com.niksob.domain.mapper.dto.auth.token.AuthTokenDetailsDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.encoded.EncodedAuthTokenDtoMapper;
import com.niksob.domain.mapper.dto.auth.token.encoded.mono.EncodedAuthTokenMonoDtoMapper;
import com.niksob.domain.mapper.dto.user.id.UserIdDtoMapper;
import com.niksob.domain.model.auth.token.details.AuthTokenDetails;
import com.niksob.domain.model.auth.token.encoded.EncodedAuthToken;
import com.niksob.domain.model.user.UserId;
import com.niksob.layer_connector.annotation.LayerConnector;
import reactor.core.publisher.Mono;

@LayerConnector(source = AuthTokenDatabaseDtoConnector.class, mapper = {
        EncodedAuthTokenDtoMapper.class,
        EncodedAuthTokenMonoDtoMapper.class,
        AuthTokenDetailsDtoMapper.class,
        UserIdDtoMapper.class
})
public interface AuthTokenDatabaseConnector {
    Mono<Boolean> existsByDetails(AuthTokenDetails authTokenDetails);

    Mono<EncodedAuthToken> load(AuthTokenDetails authTokenDetails);

    Mono<EncodedAuthToken> save(EncodedAuthToken authToken);

    Mono<EncodedAuthToken> update(EncodedAuthToken authToken);

    Mono<Void> delete(AuthTokenDetails authTokenDetails);

    Mono<Void> deleteByUserId(UserId userId);
}
