package com.niksob.domain.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "microservice.connection.database")
public class DatabaseConnectionProperties {
    private String protocol;
    private String hostname;
    private String port;
    private String path;
}

