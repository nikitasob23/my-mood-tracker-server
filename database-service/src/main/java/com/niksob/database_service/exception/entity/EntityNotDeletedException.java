package com.niksob.database_service.exception.entity;

public class EntityNotDeletedException extends RuntimeException {
    private final String username;
    public EntityNotDeletedException(String username) {
        this.username = username;
    }

    public EntityNotDeletedException(String message, String username) {
        super(message);
        this.username = username;
    }

    public EntityNotDeletedException(String message, String username, Throwable cause) {
        super(message, cause);
        this.username = username;
    }

    public EntityNotDeletedException(String username, Throwable cause) {
        super(cause);
        this.username = username;
    }

    public EntityNotDeletedException(
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
