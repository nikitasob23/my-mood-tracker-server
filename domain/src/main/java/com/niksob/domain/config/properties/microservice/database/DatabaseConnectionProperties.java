package com.niksob.domain.config.properties.microservice.database;

import com.niksob.domain.config.properties.ConnectionProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "microservice.connection.database")
public class DatabaseConnectionProperties extends ConnectionProperties {
}
