package com.niksob.domain.http.connector.microservice.database.error.handler;

import com.niksob.domain.exception.resource.*;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.domain.exception.server.error.InternalServerException;
import com.niksob.domain.http.connector.microservice.database.user.dto.UserDatabaseDtoConnectorImpl;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class DatabaseDtoConnectorErrorHandler {
    private final String entityName;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserDatabaseDtoConnectorImpl.class);

    public <T> Mono<T> createLoadingError(Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createInternalServerException(throwable, state);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.NOT_FOUND) == 0) {
            logLoadingError(state);
            return Mono.error(new ResourceNotFoundException(
                    "%s not found from the database".formatted(entityName), throwable, state)
            );
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.BAD_REQUEST) == 0) {
            logLoadingError(state);
            return Mono.error(new ResourceLoadingException(
                    "%s was not loaded from the database".formatted(entityName), state, throwable)
            );
        }
        return checkNotFoundResponse(httpClientException, throwable, state);
    }

    public <T> Mono<T> createSavingError(Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createInternalServerException(throwable, state);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.CONFLICT) == 0) {
            logSavingError(state);
            return Mono.error(new ResourceAlreadyExistsException(
                    "%s already exists".formatted(entityName), throwable, state)
            );
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.BAD_REQUEST) == 0) {
            logSavingError(state);
            return Mono.error(new ResourceSavingException(
                    "%s was not save in the database".formatted(entityName), state, throwable)
            );
        }
        return createInternalServerException(throwable, state);
    }

    public <T> Mono<T> createUpdatingError(Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createInternalServerException(throwable, state);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.BAD_REQUEST) == 0) {
            logSavingError(state);
            return Mono.error(new ResourceUpdatingException(
                    "%s was not update in the database".formatted(entityName), throwable, state)
            );
        }
        return checkNotFoundResponse(httpClientException, throwable, state);
    }

    public <T> Mono<T> createDeletionError(Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createInternalServerException(throwable, state);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.BAD_REQUEST) == 0) {
            logDeletingError(state);
            return Mono.error(new ResourceDeletionException(
                    "%s was not delete from the database".formatted(entityName), throwable, state)
            );
        }
        return checkNotFoundResponse(httpClientException, throwable, state);
    }

    public <T> Mono<T> createDeletionAllError(Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException httpClientException)) {
            return createInternalServerException(throwable, state);
        }
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.BAD_REQUEST) == 0) {
            logDeletingError(state);
            return Mono.error(new ResourceUpdatingException(
                    "All %ss was not delete by user id from the database".formatted(entityName), throwable, state)
            );
        }
        return checkNotFoundResponse(httpClientException, throwable, state);
    }

    private <T> Mono<T> checkNotFoundResponse(
            HttpClientException httpClientException, Throwable throwable, Object state
    ) {
        if (httpClientException.getHttpStatus().compareTo(HttpStatus.NOT_FOUND) == 0) {
            logLoadingError(state);
            return Mono.error(new ResourceNotFoundException(
                    "%s not found from the database".formatted(entityName), throwable, state)
            );
        }
        return createInternalServerException(throwable, state);
    }

    private void logLoadingError(Object state) {
        log.error(
                "Failed to load %s from the database".formatted(entityName), null, state
        );
    }

    private void logSavingError(Object state) {
        log.error("Failed to save %s to the database".formatted(entityName), null, state);
    }

    private void logDeletingError(Object state) {
        log.error("Failed to delete %s to the database".formatted(entityName), null, state);
    }

    private <T> Mono<T> createInternalServerException(Throwable throwable, Object state) {
        logSavingError(state);
        return Mono.error(new InternalServerException("%s saving failure".formatted(entityName), throwable));
    }
}
