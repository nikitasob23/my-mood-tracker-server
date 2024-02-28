package com.niksob.authorization_service.exception.auth.token;

public class AuthTokenException extends RuntimeException {
    public AuthTokenException(String message) {
        super(message);
    }

    public AuthTokenException(String message, Exception cause) {
        super(message, cause);
    }
}
