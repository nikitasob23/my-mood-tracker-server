package com.niksob.authorization_service.exception.auth.token.expired;

import jakarta.security.auth.message.AuthException;

public class ExpiredAuthTokenException extends RuntimeException {
    public ExpiredAuthTokenException(String message) {
        super(message);
    }

    public ExpiredAuthTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
