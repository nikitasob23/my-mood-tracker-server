package com.niksob.authorization_service.exception.auth.signup;

public class DuplicateSignupAttemptException extends RuntimeException {
    public DuplicateSignupAttemptException(String message, Throwable cause) {
        super(message, cause);
    }
}
