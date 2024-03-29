package com.niksob.authorization_service.exception.auth.token.invalid;

public class InvalidAuthTokenException extends RuntimeException {
    public InvalidAuthTokenException(String s) {
        super(s);
    }
}
