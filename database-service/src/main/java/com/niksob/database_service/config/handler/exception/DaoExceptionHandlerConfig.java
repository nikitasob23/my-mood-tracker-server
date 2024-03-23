package com.niksob.database_service.config.handler.exception;

import com.niksob.database_service.handler.exception.DaoExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoExceptionHandlerConfig {


    public static final String FAILED_GETTING_ENTITY_FROM_REPOSITORY = "Failed getting %s by %s from repository";
    public static final String FAILED_SAVING_ENTITY_TO_REPOSITORY = "Failed saving %s to repository";

    public static final String USER_NOT_FOUND_MESSAGE = "The %s was not found";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "%s already exists";

    @Bean("userDaoExceptionHandler")
    public DaoExceptionHandler getUserDaoExceptionHandler() {
        final String entityName = "user";
        final String failedGettingEntityFromRepository = FAILED_GETTING_ENTITY_FROM_REPOSITORY
                .formatted(entityName, "username");
        final String failedSavingEntityToRepository = FAILED_SAVING_ENTITY_TO_REPOSITORY.formatted(entityName);

        final String userNotFoundMessage = USER_NOT_FOUND_MESSAGE.formatted(entityName);
        final String userAlreadyExistsMessage = USER_ALREADY_EXISTS_MESSAGE.formatted(entityName);

        return new DaoExceptionHandler(
                failedGettingEntityFromRepository,
                failedSavingEntityToRepository,
                userNotFoundMessage,
                userAlreadyExistsMessage
        );
    }

    @Bean("authTokenDaoExceptionHandler")
    public DaoExceptionHandler getAuthTokenDaoExceptionHandler() {
        final String entityName = "auth token";
        final String failedGettingEntityFromRepository = FAILED_GETTING_ENTITY_FROM_REPOSITORY
                .formatted(entityName, "user id");
        final String failedSavingEntityToRepository = FAILED_SAVING_ENTITY_TO_REPOSITORY.formatted(entityName);

        final String userNotFoundMessage = USER_NOT_FOUND_MESSAGE.formatted(entityName);
        final String userAlreadyExistsMessage = USER_ALREADY_EXISTS_MESSAGE.formatted(entityName);

        return new DaoExceptionHandler(
                failedGettingEntityFromRepository,
                failedSavingEntityToRepository,
                userNotFoundMessage,
                userAlreadyExistsMessage
        );
    }
}
