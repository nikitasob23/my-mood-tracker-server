package com.niksob.domain.http.connector.auth.token.dto;

import com.niksob.domain.config.properties.DatabaseConnectionProperties;
import com.niksob.domain.dto.auth.token.encoded.EncodedAuthTokenDto;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.exception.server.error.InternalServerException;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseDatabaseConnector;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.path.controller.database_service.auth.token.AuthTokenDBControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthTokenDatabaseDtoConnectorImpl extends BaseDatabaseConnector implements AuthTokenDatabaseDtoConnector {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenDatabaseDtoConnectorImpl.class);
    public AuthTokenDatabaseDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            DatabaseConnectionProperties connectionProperties
    ) {
        super(httpClient, restPath, connectionProperties);
    }

    @Override
    public Mono<EncodedAuthTokenDto> save(EncodedAuthTokenDto authTokenDto) {
        return httpClient.sendPostRequest(
                restPath.post(connectionProperties, AuthTokenDBControllerPaths.BASE_URI),
                authTokenDto, EncodedAuthTokenDto.class, EncodedAuthTokenDto.class
        ).onErrorResume(throwable -> createSavingError(throwable, authTokenDto));
    }

    private <T> Mono<T> createSavingError(Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createInternalServerError(throwable, state);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.BAD_REQUEST) == 0) {
            final String message = "Encoded auth token info saving failure due to a bad request";
            log.error(message, null, state);
            return Mono.error(new ResourceSavingException(message, state, throwable));
        }
        return createInternalServerError(throwable, state);
    }

    private <T> Mono<T> createInternalServerError(Throwable throwable, Object state) {
        final String message = "Encoded auth token info saving failure";
        log.error(message, null, state);
        return Mono.error(new InternalServerException(message, throwable));
    }
}
