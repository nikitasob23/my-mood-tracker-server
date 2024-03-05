package com.niksob.logger.config.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:" + AppPropertiesConfig.PROPERTY_FILE_NAME)
public class AppPropertiesConfig {
    public static final String PROPERTY_FILE_NAME = "logger-config.yml";

    @Bean
    public static PropertySourcesPlaceholderConfigurer getPropertySourcesPlaceholderConfigurer() {
        return YamlPropertySourcesPlaceholderConfigurerFactory.create(PROPERTY_FILE_NAME);
    }
}
