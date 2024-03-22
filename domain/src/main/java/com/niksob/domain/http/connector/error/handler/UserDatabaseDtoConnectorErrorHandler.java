package com.niksob.domain.http.connector.error.handler;

import com.niksob.domain.dto.user.UserInfoDto;
import com.niksob.domain.exception.resource.ResourceAlreadyExistsException;
import com.niksob.domain.exception.resource.ResourceLoadingException;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.exception.server.error.InternalServerException;
import com.niksob.domain.http.connector.UserDatabaseDtoConnectorImpl;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserDatabaseDtoConnectorErrorHandler {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserDatabaseDtoConnectorImpl.class);

    public Mono<UserInfoDto> createLoadingError(Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createInternalServerException(throwable, state);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.NOT_FOUND) == 0) {
            logLoadingError(state);
            return Mono.error(new ResourceNotFoundException("Username not found from the database", throwable, state));
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.BAD_REQUEST) == 0) {
            logLoadingError(state);
            return Mono.error(new ResourceLoadingException("User was not loaded from the database", state, throwable));
        }
        return createInternalServerException(throwable, state);
    }

    public Mono<Void> createSavingError(Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createInternalServerException(throwable, state);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.CONFLICT) == 0) {
            logSavingError(state);
            return Mono.error(new ResourceAlreadyExistsException("User already exists", throwable, state));
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.BAD_REQUEST) == 0) {
            logSavingError(state);
            return Mono.error(new ResourceSavingException("User was not saved in the database", state, throwable));
        }
        return createInternalServerException(throwable, state);
    }

    private void logLoadingError(Object state) {
        log.error("Failed to load user info by username from the database", null, state);
    }

    private void logSavingError(Object state) {
        log.error("Failed to save user info to the database", null, state);
    }

    private <T> Mono<T> createInternalServerException(Throwable throwable, Object state) {
        logSavingError(state);
        return Mono.error(new InternalServerException("User info saving failure", throwable));
    }
}
