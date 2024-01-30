package com.niksob.database_service.exception.resource;

import lombok.Getter;

@Getter
public class ResourceDeletionException extends RuntimeException {
    private final String username;

    public ResourceDeletionException(String message, Throwable cause, String username) {
        super(message, cause);
        this.username = username;
    }
}
