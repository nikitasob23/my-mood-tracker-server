package com.niksob.database_service.exception.entity;

import com.niksob.domain.model.user.Username;

public class EntitySavingException extends RuntimeException {
    private final Username username;
    public EntitySavingException(Username username) {
        this.username = username;
    }

    public EntitySavingException(String message, Username username) {
        super(message);
        this.username = username;
    }

    public EntitySavingException(String message, Username username, Throwable cause) {
        super(message, cause);
        this.username = username;
    }

    public EntitySavingException(Username username, Throwable cause) {
        super(cause);
        this.username = username;
    }

    public EntitySavingException(String message, Username username, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.username = username;
    }
}
