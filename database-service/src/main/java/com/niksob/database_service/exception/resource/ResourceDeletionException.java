package com.niksob.database_service.exception.resource;

import lombok.Getter;

@Getter
public class ResourceDeletionException extends RuntimeException {
    private final Object state;

    public ResourceDeletionException(String message, Throwable cause, Object state) {
        super(message, cause);
        this.state = state;
    }
}
