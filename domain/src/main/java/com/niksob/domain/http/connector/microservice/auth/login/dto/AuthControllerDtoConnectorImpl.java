package com.niksob.domain.http.connector.microservice.auth.login.dto;

import com.niksob.domain.config.properties.microservice.auth.AuthConnectionProperties;
import com.niksob.domain.dto.auth.login.SignOutDetailsDto;
import com.niksob.domain.dto.auth.login.UserEmailDto;
import com.niksob.domain.dto.auth.login.UserPasswordPairDto;
import com.niksob.domain.dto.auth.login.active_code.ActiveCodeDto;
import com.niksob.domain.dto.user.UserIdDto;
import com.niksob.domain.dto.user.signup.SignupDetailsDto;
import com.niksob.domain.http.client.HttpClient;
import com.niksob.domain.http.connector.base.BaseConnector;
import com.niksob.domain.http.rest.path.RestPath;
import com.niksob.domain.mapper.rest.auth.token.params.AuthTokenGetParamsMapper;
import com.niksob.domain.mapper.rest.user.UserGetParamsMapper;
import com.niksob.domain.path.controller.authorization_service.AuthControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthControllerDtoConnectorImpl extends BaseConnector implements LoginControllerDtoConnector {
    private final UserGetParamsMapper userGetParamsMapper;
    private final AuthTokenGetParamsMapper authTokenGetParamsMapper;
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthControllerDtoConnectorImpl.class);

    public AuthControllerDtoConnectorImpl(
            HttpClient httpClient,
            RestPath restPath,
            AuthConnectionProperties connectionProperties,
            UserGetParamsMapper userGetParamsMapper,
            AuthTokenGetParamsMapper authTokenGetParamsMapper
    ) {
        super(httpClient, restPath, connectionProperties);
        this.userGetParamsMapper = userGetParamsMapper;
        this.authTokenGetParamsMapper = authTokenGetParamsMapper;
    }

    @Override
    public Mono<Void> signup(SignupDetailsDto signupDetails) {
        final String uri = getWithBody(AuthControllerPaths.SIGNUP);
        return httpClient.sendPostRequest(uri, signupDetails, SignupDetailsDto.class, Void.class)
                .doOnError(throwable ->
                        log.error("Login connector received failure response after signup", null, signupDetails));
    }

    @Override
    public Mono<Void> resetEmail(UserEmailDto userEmail) {
        final String uri = getWithBody(AuthControllerPaths.EMAIL_RESETTING);
        return httpClient.sendPostRequest(uri, userEmail, UserEmailDto.class, Void.class)
                .doOnError(throwable -> log.error(
                        "Login connector received failure response after resetting email",
                        null, userEmail
                ));
    }

    @Override
    public Mono<Void> resetPassword(UserPasswordPairDto userPasswordPair) {
        final String uri = getWithBody(AuthControllerPaths.PASSWORD_RESETTING);
        return httpClient.sendPostRequest(uri, userPasswordPair, UserPasswordPairDto.class, Void.class)
                .doOnError(throwable -> log.error(
                        "Login connector received failure response after resetting password",
                        null, userPasswordPair
                ));
    }

    @Override
    public Mono<Void> signupByActiveCode(ActiveCodeDto activeCode) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(activeCode);
        final String uri = getWithParams(AuthControllerPaths.ACTIVE_CODE, params);
        return httpClient.sendGetRequest(uri, Void.class);
    }

    @Override
    public Mono<Void> resetEmailByActiveCode(ActiveCodeDto activeCode) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(activeCode);
        final String uri = getWithParams(AuthControllerPaths.EMAIL_RESETTING_ACTIVATION, params);
        return httpClient.sendGetRequest(uri, Void.class);
    }

    @Override
    public Mono<Void> signOut(SignOutDetailsDto signOutDetails) {
        final Map<String, String> params = authTokenGetParamsMapper.getHttpParams(signOutDetails);
        final String uri = getWithParams(AuthControllerPaths.SIGNOUT, params);
        return httpClient.sendGetRequest(uri, Void.class)
                .doOnError(throwable -> log.error(
                        "Login connector received failure response after sign out", null, signOutDetails));
    }

    @Override
    public Mono<Void> signOutAll(UserIdDto userId) {
        final Map<String, String> params = userGetParamsMapper.getHttpParams(userId);
        final String uri = getWithParams(AuthControllerPaths.SIGNOUT_ALL, params);
        return httpClient.sendGetRequest(uri, Void.class)
                .doOnError(throwable -> log.error(
                        "Login connector received failure response after sign out from all devices",
                        null, userId
                ));
    }
}
