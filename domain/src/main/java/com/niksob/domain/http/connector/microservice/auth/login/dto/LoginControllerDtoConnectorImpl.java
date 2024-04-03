package com.niksob.domain.http.connector.microservice.auth.login.dto;

import com.niksob.domain.config.properties.microservice.auth.AuthConnectionProperties;
import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseConnector;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.mapper.rest.auth.token.params.AuthTokenGetParamsMapper;
import com.niksob.domain.path.controller.authorization_service.LoginControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class LoginControllerDtoConnectorImpl extends BaseConnector implements LoginControllerDtoConnector {
    private final AuthTokenGetParamsMapper authTokenGetParamsMapper;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(LoginControllerDtoConnectorImpl.class);

    public LoginControllerDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            AuthConnectionProperties connectionProperties,
            AuthTokenGetParamsMapper authTokenGetParamsMapper
    ) {
        super(httpClient, restPath, connectionProperties);
        this.authTokenGetParamsMapper = authTokenGetParamsMapper;
    }

    @Override
    public Mono<Void> signup(SignupDetailsDto signupDetails) {
        final String uri = getWithBody(LoginControllerPaths.SIGNUP);
        return httpClient.sendPostRequest(uri, signupDetails, SignupDetailsDto.class, Void.class)
                .doOnError(throwable ->
                        log.error("Login connector received failure response after signup", null, signupDetails));
    }

    @Override
    public Mono<Void> signOut(SignOutDetailsDto signOutDetails) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(signOutDetails);
        final String uri = getWithParams(LoginControllerPaths.SIGNOUT, params);
        return httpClient.sendGetRequest(uri, Void.class)
                .doOnError(throwable -> log.error(
                        "Login connector received failure response after sign out", null, signOutDetails));
    }

    @Override
    public Mono<Void> signOutAll(UserIdDto userId) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(userId);
        final String uri = getWithParams(LoginControllerPaths.SIGNOUT_ALL, params);
        return httpClient.sendGetRequest(uri, Void.class)
                .doOnError(throwable -> log.error(
                        "Login connector received failure response after sign out from all devices",
                        null, userId
                ));
    }
}