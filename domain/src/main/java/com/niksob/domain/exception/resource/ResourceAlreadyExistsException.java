package com.niksob.domain.exception.resource;

import lombok.Getter;

@Getter
public class ResourceAlreadyExistsException extends RuntimeException {
    private final Object state;

    public ResourceAlreadyExistsException(String message, Throwable cause, Object state) {
        super(message, cause);
        this.state = state;
    }
}
