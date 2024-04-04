package com.niksob.gateway_service.exception.security.account;

public class LockedAccountException extends RuntimeException {
    public LockedAccountException(String message) {
        super(message);
    }
}
