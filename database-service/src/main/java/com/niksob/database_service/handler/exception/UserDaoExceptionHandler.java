package com.niksob.database_service.handler.exception;

import com.niksob.domain.exception.resource.ResourceAlreadyExistsException;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDaoExceptionHandler {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(UserDaoExceptionHandler.class);

    public ResourceNotFoundException createResourceNotFoundException(Object state) {
        return createResourceNotFoundException(state, null);
    }

    public ResourceNotFoundException createResourceNotFoundException(Object state, Exception e) {
        log.error("Failed getting user by username from repository", null, state);
        return new ResourceNotFoundException("The user was not found", e, state);
    }

    public ResourceAlreadyExistsException createResourceAlreadyExistsException(Object state) {
        log.error("Failed saving user to repository", null, state);
        throw new ResourceAlreadyExistsException("User already exists", null, state);
    }
}
