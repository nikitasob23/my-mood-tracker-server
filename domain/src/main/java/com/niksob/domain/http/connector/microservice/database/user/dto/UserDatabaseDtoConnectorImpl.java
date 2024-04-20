package com.niksob.domain.http.connector.microservice.database.user.dto;

import com.niksob.domain.config.properties.microservice.database.DatabaseConnectionProperties;
import com.niksob.domain.dto.user.*;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseConnector;
import com.niksob.domain.http.connector.microservice.database.error.handler.DatabaseDtoConnectorErrorHandler;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.mapper.rest.user.UserGetParamsMapper;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class UserDatabaseDtoConnectorImpl extends BaseConnector implements UserDatabaseDtoConnector {
    private final UserGetParamsMapper userGetParamsMapper;
    private final DatabaseDtoConnectorErrorHandler errorHandler;

    public UserDatabaseDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            DatabaseConnectionProperties connectionProperties,
            UserGetParamsMapper userGetParamsMapper,
            @Qualifier("userDatabaseDtoConnectorErrorHandler")
            DatabaseDtoConnectorErrorHandler errorHandler
    ) {
        super(httpClient, restPath, connectionProperties);
        this.errorHandler = errorHandler;
        this.userGetParamsMapper = userGetParamsMapper;
    }

    @Override
    public Mono<SecurityUserDetailsDto> load(UsernameDto usernameDto) {
        final Map<String, String> params = userGetParamsMapper.getHttpParams(usernameDto);
        final String uri = getWithParams(UserControllerPaths.BASE_URI, params);
        return httpClient.sendGetRequest(uri, SecurityUserDetailsDto.class)
                .onErrorResume(throwable -> errorHandler.createLoadingError(throwable, usernameDto));
    }

    @Override
    public Mono<FullUserInfoDto> loadFull(UsernameDto usernameDto) {
        final Map<String, String> params = userGetParamsMapper.getHttpParams(usernameDto);
        final String uri = getWithParams(UserControllerPaths.BASE_URI + UserControllerPaths.FULL_USER, params);
        return httpClient.sendGetRequest(uri, FullUserInfoDto.class)
                .onErrorResume(throwable -> errorHandler.createLoadingError(throwable, usernameDto));
    }

    @Override
    public Mono<Boolean> existsByEmail(EmailDto email) {
        final String uri = getWithBody(UserControllerPaths.BASE_URI + UserControllerPaths.EMAIL);
        return httpClient.sendPostRequest(uri, email, EmailDto.class, Boolean.class)
                .onErrorResume(throwable -> errorHandler.createLoadingError(throwable, email));
    }

    @Override
    public Mono<UserInfoDto> save(UserInfoDto userInfoDto) {
        final String uri = getWithBody(UserControllerPaths.BASE_URI);
        return httpClient.sendPostRequest(uri, userInfoDto, UserInfoDto.class, UserInfoDto.class)
                .onErrorResume(throwable -> errorHandler.createSavingError(throwable, userInfoDto));
    }

    @Override
    public Mono<Void> update(UserInfoDto userInfoDto) {
        return httpClient.sendPutRequest(
                getWithBody(UserControllerPaths.BASE_URI),
                userInfoDto, UserInfoDto.class, Void.class
        ).onErrorResume(throwable -> errorHandler.createUpdatingError(throwable, userInfoDto));
    }

    @Override
    public Mono<Void> delete(UsernameDto usernameDto) {
        final Map<String, String> params = userGetParamsMapper.getHttpParams(usernameDto);
        final String uri = getWithParams(UserControllerPaths.BASE_URI, params);
        return httpClient.sendDeleteRequest(uri, Void.class)
                .onErrorResume(throwable -> errorHandler.createUpdatingError(throwable, usernameDto));
    }
}
