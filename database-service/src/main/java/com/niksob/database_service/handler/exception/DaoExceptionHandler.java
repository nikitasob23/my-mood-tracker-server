package com.niksob.database_service.handler.exception;

import com.niksob.domain.exception.resource.ResourceAlreadyExistsException;
import com.niksob.domain.exception.resource.ResourceNotFoundException;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DaoExceptionHandler {
    private final String failedGettingEntityFromRepositoryMessage;
    private final String failedSavingEntityToRepositoryMessage;

    private String userNotFoundMessage;
    private String userAlreadyExistsMessage;

    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(DaoExceptionHandler.class);

    public ResourceNotFoundException createResourceNotFoundException(Object state) {
        return createResourceNotFoundException(state, null);
    }

    public ResourceNotFoundException createResourceNotFoundException(Object state, Exception e) {
        log.error(failedGettingEntityFromRepositoryMessage, null, state);
        return new ResourceNotFoundException(userNotFoundMessage, e, state);
    }

    public ResourceAlreadyExistsException createResourceAlreadyExistsException(Object state) {
        log.error(failedSavingEntityToRepositoryMessage, null, state);
        throw new ResourceAlreadyExistsException(userAlreadyExistsMessage, null, state);
    }
}
