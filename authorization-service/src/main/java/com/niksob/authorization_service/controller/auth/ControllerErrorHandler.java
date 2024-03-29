package com.niksob.authorization_service.controller.auth;

import com.niksob.authorization_service.exception.auth.UnauthorizedAccessException;
import com.niksob.authorization_service.exception.auth.signup.DuplicateSignupAttemptException;
import com.niksob.authorization_service.exception.auth.signup.SignupException;
import com.niksob.authorization_service.exception.auth.token.AuthTokenException;
import com.niksob.authorization_service.model.login.password.WrongPasswordException;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.http.connector.error.handler.InternalServerErrorUtil;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ControllerErrorHandler {
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final InternalServerErrorUtil internalServerErrorUtil;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(ControllerErrorHandler.class);

    public <T> Mono<T> createSignupError(Throwable throwable) {
        HttpClientException errorResponse;
        if (throwable instanceof DuplicateSignupAttemptException) {
            errorResponse = new HttpClientException(throwable, HttpStatus.CONFLICT, contextPath);
        } else if (throwable instanceof SignupException) {
            errorResponse = new HttpClientException(throwable, HttpStatus.BAD_REQUEST, contextPath);
        } else {
            return internalServerErrorUtil.createMonoResponse(throwable, ControllerErrorHandler.class);
        }
        log.error("Controller returning failed response", throwable, errorResponse);
        return Mono.error(errorResponse);
    }

    public  <T> Mono<T> createAuthTokenNotGenerated(Throwable throwable) {
        HttpClientException errorResponse;
        if (throwable instanceof WrongPasswordException) {
            errorResponse = new HttpClientException(throwable, HttpStatus.BAD_REQUEST, contextPath);
        } else if (throwable instanceof UnauthorizedAccessException) {
            errorResponse = new HttpClientException(throwable, HttpStatus.NOT_FOUND, contextPath);
        } else if (throwable instanceof AuthTokenException) {
            errorResponse = new HttpClientException(throwable, HttpStatus.BAD_REQUEST, contextPath);
        } else {
            return internalServerErrorUtil.createMonoResponse(throwable, ControllerErrorHandler.class);
        }
        log.error("Controller returning failed response", null, errorResponse);
        return Mono.error(errorResponse);
    }
}
