package com.niksob.authorization_service.exception.jwt;

public class JwtDataReceivingException extends RuntimeException {
    public JwtDataReceivingException(String s, Throwable cause) {
        super(s, cause);
    }
}
