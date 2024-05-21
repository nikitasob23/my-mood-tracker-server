package com.niksob.domain.http.controller.handler.mood.entry;

import com.niksob.domain.exception.resource.*;
import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.logger.object_state.ObjectStateLogger;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ResourceControllerErrorUtil {
    private final String returnElementsName;
    private final String staticPath;

    private final ObjectStateLogger log;

    public <T> Flux<T> createResponse(String message, HttpStatus httpStatus) {
        var statusResponse = new HttpClientException(message, httpStatus, staticPath);
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
        if (throwable instanceof ResourceSavingException) {
            return createAndLogHttpClientExceptionMono(throwable, HttpStatus.BAD_REQUEST);
        } else if (throwable instanceof ResourceAlreadyExistsException) {
            return createAndLogHttpClientExceptionMono(throwable, HttpStatus.CONFLICT);
        } else if (throwable instanceof ResourceUpdatingException
                && ((ResourceUpdatingException) throwable).getResource().equals("tags")) {
            return createAndLogHttpClientExceptionMono(throwable, HttpStatus.BAD_REQUEST);
        }
        return Mono.error(createLoadingError(throwable));
    }

    public <T> Mono<T> createUpdatingError(Throwable throwable) {
        if (throwable instanceof ResourceUpdatingException) {
            final var errorResponse = createAndLogHttpClientException(throwable, HttpStatus.BAD_REQUEST);
            return Mono.error(errorResponse);
        }
        return createLoadingErrorMono(throwable);
    }

    public Mono<Void> createDeleteError(Throwable throwable) {
        if (throwable instanceof ResourceDeletionException) {
            final var errorResponse = createAndLogHttpClientException(throwable, HttpStatus.BAD_REQUEST);
            return Mono.error(errorResponse);
        }
        return createLoadingErrorMono(throwable).then();
    }

    private Throwable createLoadingError(Throwable throwable) {
        if (throwable instanceof ResourceNotFoundException) {
            return createAndLogHttpClientException(throwable, HttpStatus.NOT_FOUND);
        }
        log.error("Controller returning failed status", null, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private <T> Mono<T> createAndLogHttpClientExceptionMono(Throwable throwable, HttpStatus httpStatus) {
        final var errorResponse = createAndLogHttpClientException(throwable, httpStatus);
        return Mono.error(errorResponse);
    }

    private HttpClientException  createAndLogHttpClientException(Throwable throwable, HttpStatus httpStatus) {
        var errorResponse = new HttpClientException(throwable, httpStatus, staticPath);
        log.error("Controller returning failed response", null, errorResponse);
        return errorResponse;
    }
}