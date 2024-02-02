package com.niksob.database_service.exception.resource;

import lombok.Getter;

@Getter
public class ResourceLoadingException extends RuntimeException {
    private final Object state;

    public ResourceLoadingException(String message, Object state, Throwable cause) {
        super(message, cause);
        this.state = state;
    }
}
