package com.niksob.authorization_service.controller.auth.exception.handler;

import com.niksob.authorization_service.exception.auth.signup.DuplicateSignupAttemptException;
import com.niksob.authorization_service.exception.auth.signup.SignupException;
import com.niksob.authorization_service.exception.auth.signup.UnregisteredUserException;
import com.niksob.authorization_service.exception.auth.token.expired.ExpiredAuthTokenException;
import com.niksob.authorization_service.exception.auth.token.invalid.InvalidAuthTokenException;
import com.niksob.authorization_service.model.login.password.WrongPasswordException;
import com.niksob.domain.exception.auth.signup.SignupDetailsAlreadyExistsException;
import com.niksob.domain.exception.auth.signup.active_code.InvalidActiveCodeException;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.exception.resource.ResourceUpdatingException;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.exception.user.email.InvalidEmailException;
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
        final String message;
        if (throwable instanceof DuplicateSignupAttemptException) {
            httpStatus = HttpStatus.CONFLICT;
            message = "Duplicate signup attempt";
        } else if (throwable instanceof InvalidEmailException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = "Invalid email";
        } else if (throwable instanceof InvalidActiveCodeException) {
            httpStatus = HttpStatus.CONFLICT;
            message = throwable.getMessage();
        } else if (throwable instanceof WrongPasswordException) {
            httpStatus = HttpStatus.FORBIDDEN;
            message = throwable.getMessage();
        } else if (throwable instanceof SignupDetailsAlreadyExistsException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = throwable.getMessage();
        } else if (throwable instanceof SignupException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = "Failure signup";
        } else if (throwable instanceof ResourceNotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
            message = "Resource not found";
        } else {
            return internalServerErrorUtil.createMonoResponse(throwable, ControllerErrorHandler.class);
        }
        final Throwable errorResponse = new HttpClientException(message, throwable, httpStatus, contextPath);
        log.error("Controller returning failed response", null, errorResponse);
        return Mono.error(errorResponse);
    }

    public <T> Mono<T> createAuthTokenException(Throwable throwable) {
        final HttpStatus httpStatus;
        final String message;
        if (throwable instanceof ResourceSavingException // Auth token saving or updating exception
                || throwable instanceof ResourceUpdatingException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            message = "Incorrect request with auth token";
        } else if (throwable instanceof WrongPasswordException) {
            httpStatus = HttpStatus.FORBIDDEN;
            message = "Wrong password";
        } else if (throwable instanceof ExpiredAuthTokenException) {
            httpStatus = HttpStatus.FORBIDDEN;
            message = "Expired token";
        } else if (throwable instanceof InvalidAuthTokenException) {
            httpStatus = HttpStatus.FORBIDDEN;
            message = "Invalid token";
        } else if (throwable instanceof UnregisteredUserException) {
            httpStatus = HttpStatus.FORBIDDEN;
            message = "User not registered";
        } else {
            return internalServerErrorUtil.createMonoResponse(throwable, ControllerErrorHandler.class);
        }
        Throwable errorResponse = new HttpClientException(message, throwable, httpStatus, contextPath);
        log.error("Controller returning failed response", null, errorResponse);
        return Mono.error(errorResponse);
    }
}
