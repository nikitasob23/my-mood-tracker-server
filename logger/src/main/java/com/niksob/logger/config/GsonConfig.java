package com.niksob.logger.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.niksob.logger.json.adapter.LocalDateTimeAdapter;
import com.niksob.logger.json.strategy.JsonBackReferenceStrategy;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@AllArgsConstructor
public class GsonConfig {
    private final LocalDateTimeAdapter localDateTimeAdapter;
    private final JsonBackReferenceStrategy jsonBackReferenceStrategy;

    @Bean
    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
                .setExclusionStrategies(jsonBackReferenceStrategy)
                .create();
    }
}
