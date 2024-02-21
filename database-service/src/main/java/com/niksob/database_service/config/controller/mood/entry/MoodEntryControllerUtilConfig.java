package com.niksob.database_service.config.controller.mood.entry;

import com.niksob.database_service.controller.mood.entry.MoodEntryController;
import com.niksob.database_service.util.controller.ResourceControllerUtil;
import com.niksob.domain.path.controller.database_service.mood.entry.MoodEntryControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoodEntryControllerUtilConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ObjectStateLogger logger = ObjectStateLoggerFactory.getLogger(MoodEntryController.class);

    @Bean("moodEntryResponseUtil")
    public ResourceControllerUtil getMoodTagControllerUtil() {
        final String staticPath = "%s/%s".formatted(contextPath, MoodEntryControllerPaths.BASE_URI);
        return new ResourceControllerUtil("Mood entry", staticPath, logger);
    }
}
