package com.niksob.domain.config.http.connector.error.handler;

import com.niksob.domain.http.connector.microservice.database.error.handler.DatabaseDtoConnectorErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class DatabaseDtoConnectorErrorHandlerConfig {
    private static final String USER_ENTITY_NAME = "User info";
    private static final String AUTH_TOKEN_ENTITY_NAME = "Auth token";
    private static final String MOOD_ENTRY_ENTITY_NAME = "Mood entry";
    private static final String MOOD_TAG_ENTITY_NAME = "Mood tag";

    @Bean("userDatabaseDtoConnectorErrorHandler")
    public DatabaseDtoConnectorErrorHandler getUserDatabaseDtoConnectorErrorHandler() {
        return new DatabaseDtoConnectorErrorHandler(USER_ENTITY_NAME);
    }

    @Bean("authTokenDatabaseDtoConnectorErrorHandler")
    public DatabaseDtoConnectorErrorHandler getAuthTokenDatabaseDtoConnectorErrorHandler() {
        return new DatabaseDtoConnectorErrorHandler(AUTH_TOKEN_ENTITY_NAME);
    }

    @Bean("moodEntryDatabaseDtoConnectorErrorHandler")
    public DatabaseDtoConnectorErrorHandler getMoodEntryDatabaseDtoConnectorErrorHandler() {
        return new DatabaseDtoConnectorErrorHandler(MOOD_ENTRY_ENTITY_NAME);
    }

    @Bean("moodTagDatabaseDtoConnectorErrorHandler")
    public DatabaseDtoConnectorErrorHandler getMoodTagDatabaseDtoConnectorErrorHandler() {
        return new DatabaseDtoConnectorErrorHandler(MOOD_TAG_ENTITY_NAME);
    }
}