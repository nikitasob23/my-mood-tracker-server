package com.niksob.authorization_service.exception.auth.token.saving;

public class AuthTokenSavingException extends RuntimeException {
    public AuthTokenSavingException(String message, Throwable cause) {
        super(message, cause);
    }
}
