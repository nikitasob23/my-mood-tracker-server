package com.niksob.domain.http.connector.auth.token.dto;

import com.niksob.domain.config.properties.DatabaseConnectionProperties;
import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseDatabaseConnector;
import com.niksob.domain.http.connector.error.handler.DatabaseDtoConnectorErrorHandler;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.mapper.rest.auth.token.params.AuthTokenGetParamsMapper;
import com.niksob.domain.path.controller.database_service.auth.token.AuthTokenDBControllerPaths;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthTokenDatabaseDtoConnectorImpl extends BaseDatabaseConnector implements AuthTokenDatabaseDtoConnector {
    private final AuthTokenGetParamsMapper authTokenGetParamsMapper;
    private final DatabaseDtoConnectorErrorHandler errorHandler;

    public AuthTokenDatabaseDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            DatabaseConnectionProperties connectionProperties,
            AuthTokenGetParamsMapper authTokenGetParamsMapper,
            @Qualifier("authTokenDatabaseDtoConnectorErrorHandler")
            DatabaseDtoConnectorErrorHandler errorHandler
    ) {
        super(httpClient, restPath, connectionProperties);
        this.authTokenGetParamsMapper = authTokenGetParamsMapper;
        this.errorHandler = errorHandler;
    }

    @Override
    public Mono<Boolean> existsByDetails(AuthTokenDetailsDto authTokenDetails) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(authTokenDetails);
        final String baseUri = AuthTokenDBControllerPaths.BASE_URI + AuthTokenDBControllerPaths.EXISTS;
        final String uri = restPath.getWithParams(connectionProperties, baseUri, params);
        return httpClient.sendGetRequest(uri, Boolean.class)
                .onErrorResume(throwable -> errorHandler.createLoadingError(throwable, authTokenDetails));
    }

    @Override
    public Mono<EncodedAuthTokenDto> save(EncodedAuthTokenDto authToken) {
        return httpClient.sendPostRequest(
                restPath.getWithBody(connectionProperties, AuthTokenDBControllerPaths.BASE_URI),
                authToken, EncodedAuthTokenDto.class, EncodedAuthTokenDto.class
        ).onErrorResume(throwable -> errorHandler.createSavingError(throwable, authToken));
    }

    @Override
    public Mono<EncodedAuthTokenDto> update(EncodedAuthTokenDto authToken) {
        return httpClient.sendPutRequest(
                restPath.getWithBody(connectionProperties, AuthTokenDBControllerPaths.BASE_URI),
                authToken, EncodedAuthTokenDto.class, EncodedAuthTokenDto.class
        ).onErrorResume(throwable -> errorHandler.createUpdatingError(throwable, authToken));
    }

    @Override
    public Mono<Void> delete(AuthTokenDetailsDto authTokenDetails) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(authTokenDetails);
        final String uri = restPath.getWithParams(connectionProperties, AuthTokenDBControllerPaths.BASE_URI, params);
        return httpClient.sendDeleteRequest(uri, Void.class)
                .onErrorResume(throwable -> errorHandler.createDeletionError(throwable, authTokenDetails));
    }
}
