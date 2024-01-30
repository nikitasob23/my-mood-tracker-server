package com.niksob.database_service.exception.resource;

import lombok.Getter;

@Getter
public class ResourceSavingException extends RuntimeException {
    private final String username;

    public ResourceSavingException(String message, String username, Throwable cause) {
        super(message, cause);
        this.username = username;
    }
}
