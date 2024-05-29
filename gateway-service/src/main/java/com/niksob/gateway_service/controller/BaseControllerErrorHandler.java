package com.niksob.gateway_service.controller;

import com.niksob.domain.exception.rest.controller.response.HttpClientException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

public class BaseControllerErrorHandler {
    @Value("${spring.webflux.base-path}")
    private String basePath;

    private final ObjectStateLogger log;

    public BaseControllerErrorHandler(Class<?> clazz) {
        this.log = ObjectStateLoggerFactory.getLogger(clazz);
    }

    public <T> Mono<T> baseError(String message, @NonNull Throwable throwable, Object state) {
        if (!(throwable instanceof HttpClientException e)) {
            return Mono.error(throwable);
        }
        e.setPath(basePath);
        log.error(message, null, state);
        return Mono.error(e);
    }
}
