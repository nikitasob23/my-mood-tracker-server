package com.niksob.logger.config.json.masked_fields;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "logger.message.masked")
@Data
public class MaskedFieldsConfig {
    private List<String> fieldNames;
}
