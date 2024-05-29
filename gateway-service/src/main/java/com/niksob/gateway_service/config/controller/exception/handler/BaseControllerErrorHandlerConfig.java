package com.niksob.gateway_service.config.controller.exception.handler;

import com.niksob.gateway_service.controller.BaseControllerErrorHandler;
import com.niksob.gateway_service.controller.auth.login.AuthController;
import com.niksob.gateway_service.controller.auth.token.AuthTokenController;
import com.niksob.gateway_service.controller.user.UserController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseControllerErrorHandlerConfig {
    @Bean("authControllerErrorHandler")
    public BaseControllerErrorHandler getAuthControllerErrorHandler() {
        return new BaseControllerErrorHandler(AuthController.class);
    }

    @Bean("authTokenControllerErrorHandler")
    public BaseControllerErrorHandler getAuthTokenControllerErrorHandler() {
        return new BaseControllerErrorHandler(AuthTokenController.class);
    }

    @Bean("userControllerErrorHandler")
    public BaseControllerErrorHandler getUserControllerErrorHandler() {
        return new BaseControllerErrorHandler(UserController.class);
    }
}
