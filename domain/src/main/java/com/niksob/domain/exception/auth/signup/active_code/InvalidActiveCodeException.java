package com.niksob.domain.exception.auth.signup.active_code;

public class InvalidActiveCodeException extends RuntimeException {
    public InvalidActiveCodeException(String message) {
        super(message);
    }
}
