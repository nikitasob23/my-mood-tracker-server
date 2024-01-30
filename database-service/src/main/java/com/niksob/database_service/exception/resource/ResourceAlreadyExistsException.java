package com.niksob.database_service.exception.resource;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {
    private final String username;

    public ResourceAlreadyExistsException(String message, Throwable cause, String username) {
        super(message, cause);
        this.username = username;
    }
}
