package com.niksob.database_service.config.controller.user;

import com.niksob.database_service.controller.user.UserController;
import com.niksob.database_service.util.controller.ResourceControllerUtil;
import com.niksob.domain.path.controller.database_service.user.UserControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserControllerUtilConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ObjectStateLogger logger = ObjectStateLoggerFactory.getLogger(UserController.class);

    @Bean("userControllerUtil")
    public ResourceControllerUtil getMoodTagControllerUtil() {
        final String staticPath = "%s/%s".formatted(contextPath, UserControllerPaths.BASE_URI);
        return new ResourceControllerUtil(null, staticPath, logger);
    }
}
