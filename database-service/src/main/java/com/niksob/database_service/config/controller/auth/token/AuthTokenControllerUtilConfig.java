package com.niksob.database_service.config.controller.auth.token;

import com.niksob.database_service.controller.user.UserController;
import com.niksob.database_service.util.controller.ResourceControllerErrorUtil;
import com.niksob.domain.path.controller.database_service.auth.token.AuthTokenDBControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthTokenControllerUtilConfig {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final ObjectStateLogger logger = ObjectStateLoggerFactory.getLogger(UserController.class);

    @Bean("authTokenControllerUtil")
    public ResourceControllerErrorUtil getUserControllerUtil() {
        final String staticPath = "%s/%s".formatted(contextPath, AuthTokenDBControllerPaths.BASE_URI);
        return new ResourceControllerErrorUtil(null, staticPath, logger);
    }
}

