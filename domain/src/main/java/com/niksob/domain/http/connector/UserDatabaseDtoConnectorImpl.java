package com.niksob.domain.http.connector;

import com.niksob.domain.config.properties.DatabaseConnectionProperties;
import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.dto.user.UsernameDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseDatabaseConnector;
import com.niksob.domain.http.connector.error.handler.DatabaseDtoConnectorErrorHandler;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.mapper.rest.user.UserGetParamsMapper;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class UserDatabaseDtoConnectorImpl extends BaseDatabaseConnector implements UserDatabaseDtoConnector {
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
    public Mono<UserInfoDto> load(UsernameDto usernameDto) {
        final Map<String, String> params = userGetParamsMapper.getHttpParams(usernameDto);
        final String uri = restPath.getWithParams(connectionProperties, UserControllerPaths.BASE_URI, params);
        return httpClient.sendGetRequest(uri, UserInfoDto.class)
                .onErrorResume(throwable -> errorHandler.createLoadingError(throwable, usernameDto));
    }

    @Override
    public Mono<Void> save(UserInfoDto userInfoDto) {
        return httpClient.sendPostRequest(
                restPath.getWithBody(connectionProperties, UserControllerPaths.BASE_URI),
                userInfoDto, UserInfoDto.class, Void.class
        ).onErrorResume(throwable -> errorHandler.createSavingError(throwable, userInfoDto));
    }
}
