package com.niksob.database_service.exception.resource;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final Object sourceParam;

    public ResourceNotFoundException(String message, Throwable cause, Object sourceParam) {
        super(message, cause);
        this.sourceParam = sourceParam;
    }
}
