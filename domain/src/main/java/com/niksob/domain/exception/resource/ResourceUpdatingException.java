package com.niksob.domain.exception.resource;

import lombok.Getter;

@Getter
public class ResourceUpdatingException extends RuntimeException {
    private final Object state;

    public ResourceUpdatingException(String message, Throwable cause, Object state) {
        super(message, cause);
        this.state = state;
    }
}

