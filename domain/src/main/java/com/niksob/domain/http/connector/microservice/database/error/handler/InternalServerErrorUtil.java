package com.niksob.domain.http.connector.microservice.database.error.handler;

import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class InternalServerErrorUtil {
    public <T, R> Mono<T> createMonoResponse(Throwable throwable, Class<R> logerNameClass) {
        var log = ObjectStateLoggerFactory.getLogger(logerNameClass);
        log.error("Controller returning failed status", throwable, HttpStatus.INTERNAL_SERVER_ERROR);
        return Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
