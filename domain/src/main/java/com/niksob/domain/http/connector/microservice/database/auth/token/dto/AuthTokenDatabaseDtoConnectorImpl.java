package com.niksob.domain.http.connector.microservice.database.auth.token.dto;

import com.niksob.domain.config.properties.ConnectionProperties;
import com.niksob.domain.dto.auth.token.details.AuthTokenDetailsDto;
import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseConnector;
import com.niksob.domain.http.connector.microservice.database.error.handler.DatabaseDtoConnectorErrorHandler;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.mapper.rest.auth.token.params.AuthTokenGetParamsMapper;
import com.niksob.domain.path.controller.database_service.auth.token.AuthTokenDBControllerPaths;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthTokenDatabaseDtoConnectorImpl extends BaseConnector implements AuthTokenDatabaseDtoConnector {
    private final AuthTokenGetParamsMapper authTokenGetParamsMapper;
    private final DatabaseDtoConnectorErrorHandler errorHandler;

    public AuthTokenDatabaseDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            ConnectionProperties connectionProperties,
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
        final String uri = getWithParams(baseUri, params);
        return httpClient.sendGetRequest(uri, Boolean.class)
                .onErrorResume(throwable -> errorHandler.createLoadingError(throwable, authTokenDetails));
    }

    @Override
    public Mono<EncodedAuthTokenDto> load(AuthTokenDetailsDto authTokenDetails) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(authTokenDetails);
        final String uri = getWithParams(AuthTokenDBControllerPaths.BASE_URI, params);
        return httpClient.sendGetRequest(uri, EncodedAuthTokenDto.class)
                .onErrorResume(throwable -> errorHandler.createLoadingError(throwable, authTokenDetails));
    }

    @Override
    public Mono<EncodedAuthTokenDto> save(EncodedAuthTokenDto authToken) {
        return httpClient.sendPostRequest(
                getWithBody(AuthTokenDBControllerPaths.BASE_URI),
                authToken, EncodedAuthTokenDto.class, EncodedAuthTokenDto.class
        ).onErrorResume(throwable -> errorHandler.createSavingError(throwable, authToken));
    }

    @Override
    public Mono<EncodedAuthTokenDto> update(EncodedAuthTokenDto authToken) {
        return httpClient.sendPutRequest(
                getWithBody(AuthTokenDBControllerPaths.BASE_URI),
                authToken, EncodedAuthTokenDto.class, EncodedAuthTokenDto.class
        ).onErrorResume(throwable -> errorHandler.createUpdatingError(throwable, authToken));
    }

    @Override
    public Mono<Void> delete(AuthTokenDetailsDto authTokenDetails) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(authTokenDetails);
        final String uri = getWithParams(AuthTokenDBControllerPaths.BASE_URI, params);
        return httpClient.sendDeleteRequest(uri, Void.class)
                .onErrorResume(throwable -> errorHandler.createDeletionError(throwable, authTokenDetails));
    }

    @Override
    public Mono<Void> deleteByUserId(UserIdDto userId) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(userId);
        final String resourceUri = AuthTokenDBControllerPaths.BASE_URI + AuthTokenDBControllerPaths.ALL;
        final String uri = getWithParams(resourceUri, params);
        return httpClient.sendDeleteRequest(uri, Void.class)
                .onErrorResume(throwable -> errorHandler.createDeletionAllError(throwable, userId));
    }
}
