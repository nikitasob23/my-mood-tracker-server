package com.niksob.authorization_service.exception.auth.token.expired;

public class ExpiredAuthTokenException extends RuntimeException {
    public ExpiredAuthTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
