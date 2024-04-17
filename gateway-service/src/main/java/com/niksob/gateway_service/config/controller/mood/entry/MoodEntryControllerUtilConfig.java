package com.niksob.gateway_service.config.controller.mood.entry;

import com.niksob.domain.http.controller.handler.mood.entry.ResourceControllerErrorUtil;
import com.niksob.gateway_service.path.controller.mood.entry.MoodEntryControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoodEntryControllerUtilConfig {

    @Value("${spring.webflux.base-path}")
    private String contextPath;

    private final ObjectStateLogger logger = ObjectStateLoggerFactory.getLogger(ResourceControllerErrorUtil.class);

    @Bean("moodEntryControllerUtil")
    public ResourceControllerErrorUtil getMoodTagControllerUtil() {
        final String staticPath = "%s/%s".formatted(contextPath, MoodEntryControllerPaths.BASE_URI);
        return new ResourceControllerErrorUtil("Mood entry", staticPath, logger);
    }
}
