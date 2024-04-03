package com.niksob.domain.http.connector.microservice.auth.login.dto;

import com.niksob.domain.config.properties.microservice.auth.AuthConnectionProperties;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseConnector;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.path.controller.authorization_service.LoginControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LoginControllerDtoConnectorImpl extends BaseConnector implements LoginControllerDtoConnector {

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(LoginControllerDtoConnectorImpl.class);

    public LoginControllerDtoConnectorImpl(
            HttpClient httpClient, RestPath restPath, AuthConnectionProperties connectionProperties
    ) {
        super(httpClient, restPath, connectionProperties);
    }

    @Override
    public Mono<Void> signup(SignupDetailsDto signupDetails) {
        final String uri = getWithBody(LoginControllerPaths.SIGNUP);
        return httpClient.sendPostRequest(uri, signupDetails, SignupDetailsDto.class, Void.class)
                .doOnError(throwable ->
                        log.error("Login connector received failure response", null, signupDetails));
    }
}
