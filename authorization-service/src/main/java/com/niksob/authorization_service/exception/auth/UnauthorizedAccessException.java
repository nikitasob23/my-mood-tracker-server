package com.niksob.authorization_service.exception.auth;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
