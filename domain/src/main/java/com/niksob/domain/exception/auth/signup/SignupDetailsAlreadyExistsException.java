package com.niksob.domain.exception.auth.signup;

public class SignupDetailsAlreadyExistsException extends RuntimeException {
    public SignupDetailsAlreadyExistsException(String message) {
        super(message);
    }
}
