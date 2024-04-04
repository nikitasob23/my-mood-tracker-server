package com.niksob.gateway_service.exception.token.access.request;

public class AccessTokenNotFoundException extends RuntimeException {
    public AccessTokenNotFoundException(String message) {
        super(message);
    }
}
