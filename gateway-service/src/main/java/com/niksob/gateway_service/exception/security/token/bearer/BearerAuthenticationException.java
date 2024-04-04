package com.niksob.gateway_service.exception.security.token.bearer;

import org.springframework.security.core.AuthenticationException;

public class BearerAuthenticationException extends AuthenticationException {
    public BearerAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
