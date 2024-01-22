package com.niksob.database_service.exception.entity;

public class EntityAlreadyExistsException extends RuntimeException {
    private final String username;

    public EntityAlreadyExistsException(String username) {
        this.username = username;
    }

    public EntityAlreadyExistsException(String message, String username) {
        super(message);
        this.username = username;
    }

    public EntityAlreadyExistsException(String message, String username, Throwable cause) {
        super(message, cause);
        this.username = username;
    }

    public EntityAlreadyExistsException(String username, Throwable cause) {
        super(cause);
        this.username = username;
    }

    public EntityAlreadyExistsException(
            String message,
            String username,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.username = username;
    }
}
