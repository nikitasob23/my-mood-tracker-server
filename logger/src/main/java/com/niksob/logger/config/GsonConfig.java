package com.niksob.logger.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.niksob.logger.adapter.json.LocalDateTimeAdapter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@AllArgsConstructor
public class GsonConfig {
    private final LocalDateTimeAdapter localDateTimeAdapter;

    @Bean
    public Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
                .create();
    }
}
