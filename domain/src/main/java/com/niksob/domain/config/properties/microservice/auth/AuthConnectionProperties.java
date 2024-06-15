package com.niksob.domain.config.properties.microservice.auth;

import com.niksob.domain.config.properties.ConnectionProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "microservice.connection.authorization")
public class AuthConnectionProperties extends ConnectionProperties {
}
