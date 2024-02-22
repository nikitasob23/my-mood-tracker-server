package com.niksob.database_service.config.controller.mood.tag;

import com.niksob.database_service.controller.mood.tag.MoodTagController;
import com.niksob.database_service.util.controller.ResourceControllerUtil;
import com.niksob.domain.path.controller.database_service.mood.tag.MoodTagControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoodTagControllerUtilConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ObjectStateLogger logger = ObjectStateLoggerFactory.getLogger(MoodTagController.class);

    @Bean("moodTagControllerUtil")
    public ResourceControllerUtil getMoodTagControllerUtil() {
        final String staticPath = "%s/%s".formatted(contextPath, MoodTagControllerPaths.BASE_URI);
        return new ResourceControllerUtil("Mood tag", staticPath, logger);
    }
}
