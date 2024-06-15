package com.niksob.gateway_service.config.controller.user;

import com.niksob.domain.http.controller.handler.mood.entry.ResourceControllerErrorUtil;
import com.niksob.gateway_service.path.controller.user.UserControllerPaths;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.factory.ObjectStateLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserControllerUtilConfig {
    @Value("${spring.webflux.base-path}")
    private String contextPath;

    private final ObjectStateLogger logger = ObjectStateLoggerFactory.getLogger(ResourceControllerErrorUtil.class);

    @Bean("userControllerUtil")
    public ResourceControllerErrorUtil getUserControllerUtil() {
        final String staticPath = contextPath + UserControllerPaths.BASE_URI;
        return new ResourceControllerErrorUtil("User", staticPath, logger);
    }
}
