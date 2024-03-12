package com.niksob.authorization_service.service.auth.token.error.handler;

import com.niksob.authorization_service.exception.auth.UnauthorizedAccessException;
import com.niksob.authorization_service.exception.auth.token.AuthTokenException;
import com.niksob.authorization_service.service.auth.token.AuthTokenServiceImpl;
import com.niksob.domain.exception.resource.ResourceLoadingException;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.domain.model.auth.token.AuthToken;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthTokenServiceErrorHandler {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthTokenServiceImpl.class);

    public Mono<AuthToken> createGeneratingError(Throwable throwable, Object state) {
        Throwable e;
        if (throwable instanceof ResourceNotFoundException) {
            e = new UnauthorizedAccessException("An unregistered user is trying to get an auth token", throwable);
        } else if (throwable instanceof ResourceLoadingException) {
            e = new AuthTokenException(throwable.getMessage(), throwable);
        } else {
            e = throwable;
        }
        log.error("Auth token generation failure", e, state);
        return Mono.error(e);
    }
}
