package com.niksob.gateway_service.controller;

import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.NonNull;
import reactor.core.publisher.Mono;

public class BaseControllerErrorHandler {
    private final String path;

    private final ObjectStateLogger log;

    public BaseControllerErrorHandler(Class<?> clazz, String path) {
        this.log = ObjectStateLoggerFactory.getLogger(clazz);
        this.path = path;
    }

    public <T> Mono<T> baseError(String message, @NonNull Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException e)) {
            return Mono.error(throwable);
        }
        e.setPath(path);
        log.error(message, null, state);
        return Mono.error(e);
    }
}
