package com.niksob.authorization_service.exception.auth.signup;

public class UnregisteredUserException extends RuntimeException {
    public UnregisteredUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
