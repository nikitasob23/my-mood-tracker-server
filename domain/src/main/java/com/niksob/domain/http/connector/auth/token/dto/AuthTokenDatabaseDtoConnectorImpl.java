package com.niksob.domain.http.connector.auth.token.dto;

import com.niksob.domain.config.properties.DatabaseConnectionProperties;
import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseDatabaseConnector;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.path.controller.authorization_service.AuthTokenControllerPaths;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthTokenDatabaseDtoConnectorImpl extends BaseDatabaseConnector implements AuthTokenDatabaseDtoConnector {

    public AuthTokenDatabaseDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            DatabaseConnectionProperties connectionProperties
    ) {
        super(httpClient, restPath, connectionProperties);
    }

    @Override
    public Mono<Void> save(AuthTokenDto authTokenDto) {
        return httpClient.sendPostRequest(
                restPath.post(connectionProperties, AuthTokenControllerPaths.BASE_URI),
                authTokenDto, AuthTokenDto.class, Void.class);
//        ).onErrorResume(throwable -> );
    }
}
