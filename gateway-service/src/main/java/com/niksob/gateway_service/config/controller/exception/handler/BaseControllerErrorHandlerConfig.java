package com.niksob.gateway_service.config.controller.exception.handler;

import com.niksob.domain.path.controller.gateway_service.AuthControllerPaths;
import com.niksob.gateway_service.controller.BaseControllerErrorHandler;
import com.niksob.gateway_service.controller.auth.login.AuthController;
import com.niksob.gateway_service.controller.auth.token.AuthTokenController;
import com.niksob.gateway_service.controller.user.UserController;
import com.niksob.gateway_service.path.controller.auth.token.AuthTokenControllerPaths;
import com.niksob.gateway_service.path.controller.user.UserControllerPaths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseControllerErrorHandlerConfig {
    @Value("${spring.webflux.base-path}")
    private String basePath;

    @Bean("authControllerErrorHandler")
    public BaseControllerErrorHandler getAuthControllerErrorHandler() {
        return new BaseControllerErrorHandler(AuthController.class, basePath + AuthControllerPaths.BASE_URI);
    }

    @Bean("authTokenControllerErrorHandler")
    public BaseControllerErrorHandler getAuthTokenControllerErrorHandler() {
        final String path = basePath + AuthTokenControllerPaths.BASE_URI;
        return new BaseControllerErrorHandler(AuthTokenController.class, path);
    }

    @Bean("userControllerErrorHandler")
    public BaseControllerErrorHandler getUserControllerErrorHandler() {
        return new BaseControllerErrorHandler(UserController.class, basePath + UserControllerPaths.BASE_URI);
    }
}
