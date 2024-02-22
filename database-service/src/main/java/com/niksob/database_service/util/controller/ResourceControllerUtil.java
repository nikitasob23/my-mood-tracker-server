package com.niksob.database_service.util.controller;

import com.niksob.database_service.exception.resource.*;
import com.niksob.domain.exception.rest.controller.response.ControllerResponseException;
import com.niksob.logger.object_state.ObjectStateLogger;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ResourceControllerUtil {
    private final String returnElementsName;
    private final String staticPath;

    private final ObjectStateLogger log;

    public <T> Flux<T> createResponse(String message, HttpStatus httpStatus) {
        var statusResponse = new ControllerResponseException(message, httpStatus, staticPath);
        log.debug("Controller returning success response", statusResponse);
        return Flux.error(statusResponse);
    }

    public <T> Flux<T> returnNoContentStatus() {
        return createResponse("%s list is empty".formatted(returnElementsName), HttpStatus.NO_CONTENT);
    }

    public <T> Mono<T> createLoadingErrorMono(Throwable throwable) {
        return Mono.error(createLoadingError(throwable));
    }

    public <T> Flux<T> createLoadingErrorFlux(Throwable throwable) {
        return Flux.error(createLoadingError(throwable));
    }

    public <T> Mono<T> createSavingError(Throwable throwable) {
        ControllerResponseException errorResponse;
        if (throwable instanceof ResourceSavingException) {
            errorResponse = new ControllerResponseException(throwable, HttpStatus.BAD_REQUEST, staticPath);
        } else if (throwable instanceof ResourceAlreadyExistsException) {
            errorResponse = new ControllerResponseException(throwable, HttpStatus.CONFLICT, staticPath);
        } else {
            log.error("Controller returning failed status", throwable, HttpStatus.INTERNAL_SERVER_ERROR);
            return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        log.error("Controller returning failed response", throwable, errorResponse);
        return Mono.error(errorResponse);
    }

    public Mono<Void> createUpdatingError(Throwable throwable) {
        if (throwable instanceof ResourceUpdatingException) {
            var errorResponse = new ControllerResponseException(throwable, HttpStatus.BAD_REQUEST, staticPath);
            log.error("Controller returning failed response", null, errorResponse);
            return Mono.error(errorResponse);
        }
        return createLoadingErrorMono(throwable).then();
    }

    public Mono<Void> createDeleteError(Throwable throwable) {
        if (throwable instanceof ResourceDeletionException) {
            var errorResponse = new ControllerResponseException(throwable, HttpStatus.BAD_REQUEST, staticPath);
            log.error("Controller returning failed response", null, errorResponse);
            return Mono.error(errorResponse);
        }
        return createLoadingErrorMono(throwable).then();
    }

    private Throwable createLoadingError(Throwable throwable) {
        Throwable errorResponse;
        if (throwable instanceof ResourceNotFoundException) {
            errorResponse = new ControllerResponseException(throwable, HttpStatus.NOT_FOUND, staticPath);
            log.error("Controller returning failed response", null, errorResponse);
            return errorResponse;
        }
        log.error("Controller returning failed status", null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}