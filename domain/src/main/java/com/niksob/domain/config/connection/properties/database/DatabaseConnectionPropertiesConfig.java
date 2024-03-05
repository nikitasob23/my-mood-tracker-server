package com.niksob.domain.config.connection.properties.database;

import com.niksob.domain.model.connection.properties.RestConnectionProperties;
import com.niksob.domain.util.rest.properties.ConnectionPropertiesUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class DatabaseConnectionPropertiesConfig {
    public static final String PROPERTY_FILE_NAME = "database-service.yml";

    private final ConnectionPropertiesUtil connectionPropertiesUtil;

    @Bean("databaseConnectionProperties")
    public RestConnectionProperties getDatabaseProperties() {
        return connectionPropertiesUtil.getProperties(PROPERTY_FILE_NAME);
    }
}
