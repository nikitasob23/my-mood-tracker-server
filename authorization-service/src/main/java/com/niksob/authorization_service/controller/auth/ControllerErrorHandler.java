package com.niksob.authorization_service.controller.auth;

import com.niksob.authorization_service.exception.auth.UnauthorizedAccessException;
import com.niksob.authorization_service.exception.auth.signup.DuplicateSignupAttemptException;
import com.niksob.authorization_service.exception.auth.signup.SignupException;
import com.niksob.authorization_service.exception.auth.token.expired.ExpiredAuthTokenException;
import com.niksob.authorization_service.exception.auth.token.invalid.InvalidAuthTokenException;
import com.niksob.authorization_service.model.login.password.WrongPasswordException;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.exception.resource.ResourceUpdatingException;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.http.connector.microservice.database.error.handler.InternalServerErrorUtil;
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

    public <T> Mono<T> createLoginError(Throwable throwable) {
        final HttpStatus httpStatus;
        if (throwable instanceof DuplicateSignupAttemptException) {
            httpStatus = HttpStatus.CONFLICT;
        } else if (throwable instanceof SignupException) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (throwable instanceof ResourceNotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else {
            return internalServerErrorUtil.createMonoResponse(throwable, ControllerErrorHandler.class);
        }
        final Throwable errorResponse = new HttpClientException(throwable, httpStatus, contextPath);
        log.error("Controller returning failed response", null, errorResponse);
        return Mono.error(errorResponse);
    }

    public <T> Mono<T> createAuthTokenNotGenerated(Throwable throwable) {
        final HttpStatus httpStatus;
        if (throwable instanceof UnauthorizedAccessException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (throwable instanceof ResourceSavingException // Auth token saving or updating exception
                || throwable instanceof ResourceUpdatingException) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (throwable instanceof WrongPasswordException
                || throwable instanceof ExpiredAuthTokenException
                || throwable instanceof InvalidAuthTokenException) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else {
            return internalServerErrorUtil.createMonoResponse(throwable, ControllerErrorHandler.class);
        }
        Throwable errorResponse = new HttpClientException(throwable, httpStatus, contextPath);
        log.error("Controller returning failed response", null, errorResponse);
        return Mono.error(errorResponse);
    }
}
