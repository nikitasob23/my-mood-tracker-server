package com.niksob.database_service.exception.entity;

public class EntitySavingException extends RuntimeException {
    private final String username;
    public EntitySavingException(String username) {
        this.username = username;
    }

    public EntitySavingException(String message, String username) {
        super(message);
        this.username = username;
    }

    public EntitySavingException(String message, String username, Throwable cause) {
        super(message, cause);
        this.username = username;
    }

    public EntitySavingException(String username, Throwable cause) {
        super(cause);
        this.username = username;
    }

    public EntitySavingException(String message, String username, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.username = username;
    }
}
