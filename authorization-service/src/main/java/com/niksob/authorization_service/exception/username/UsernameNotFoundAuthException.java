package com.niksob.authorization_service.exception.username;

import jakarta.security.auth.message.AuthException;

public class UsernameNotFoundAuthException extends AuthException {
    public UsernameNotFoundAuthException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
