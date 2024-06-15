package com.niksob.authorization_service.exception.jwt;

public class InvalidJwtException extends RuntimeException {
    public InvalidJwtException(String s, Throwable cause) {
        super(s, cause);
    }
}
