package com.niksob.domain.http.connector.microservice.auth.token.dto;

import com.niksob.domain.config.properties.microservice.auth.AuthConnectionProperties;
import com.niksob.domain.dto.auth.login.RowLoginInDetailsDto;
import com.niksob.domain.dto.auth.token.AuthTokenDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseConnector;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.path.controller.authorization_service.AuthTokenControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthTokenDtoConnectorImpl extends BaseConnector implements AuthTokenDtoConnector {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenDtoConnectorImpl.class);

    public AuthTokenDtoConnectorImpl(
            HttpClient httpClient, RestPath restPath, AuthConnectionProperties connectionProperties
    ) {
        super(httpClient, restPath, connectionProperties);
    }

    @Override
    public Mono<AuthTokenDto> generate(RowLoginInDetailsDto rowLoginInDetails) {
        final String uri = getWithBody(AuthTokenControllerPaths.BASE_URI);
        return httpClient.sendPostRequest(uri, rowLoginInDetails, RowLoginInDetailsDto.class, AuthTokenDto.class)
                .doOnError(throwable ->
                        log.error("Auth token connector received failure response", null, rowLoginInDetails));
    }
}
