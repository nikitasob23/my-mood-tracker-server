package com.niksob.domain.exception.resource;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final Object state;

    public ResourceNotFoundException(String message, Throwable cause, Object state) {
        super(message, cause);
        this.state = state;
    }
}
