package com.niksob.database_service.exception.resource;

import lombok.Getter;

@Getter
public class ResourceUpdatingException extends RuntimeException {
    private final Object state;

    public ResourceUpdatingException(String message, Object state) {
        super(message);
        this.state = state;
    }

    public ResourceUpdatingException(String message, Throwable cause, Object state) {
        super(message, cause);
        this.state = state;
    }
}

