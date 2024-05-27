package com.niksob.authorization_service.service.auth.handler;

import com.niksob.authorization_service.exception.auth.signup.DuplicateSignupAttemptException;
import com.niksob.authorization_service.exception.auth.signup.SignupException;
import com.niksob.authorization_service.model.login.password.WrongPasswordException;
import com.niksob.domain.exception.resource.ResourceAlreadyExistsException;
import com.niksob.domain.exception.resource.ResourceSavingException;
import com.niksob.domain.exception.user.email.InvalidEmailException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthExceptionHandler {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(AuthExceptionHandler.class);

    public  <T> Mono<T> createIncorrectEmailMonoError(Object state) {
        final String message = "Incorrect email";
        var e = new InvalidEmailException(message);
        log.error(message, e, state);
        return Mono.error(e);
    }

    public  <T> Mono<T> createIncorrectPasswordMonoError(Object state) {
        final String message = "Incorrect password";
        var e = new WrongPasswordException(message);
        log.error(message, e, state);
        return Mono.error(e);
    }

    public  <T> Mono<T> createResettingEmailMonoError(Throwable throwable, Object state) {
        final String message = "Resetting email is failed";
        log.error(message, throwable, state);
        return Mono.error(throwable);
    }

    public  <T> Mono<T> createSignupError(Throwable throwable, Object state) {
        Throwable e;
        if (throwable instanceof ResourceAlreadyExistsException) {
            e = new DuplicateSignupAttemptException("Duplicate signup attempt", throwable);
        } else if (throwable instanceof ResourceSavingException) {
            e = new SignupException(throwable.getMessage(), throwable);
        } else {
            e = throwable;
        }
        log.error("Signup failure", throwable, state);
        return Mono.error(e);
    }
}
