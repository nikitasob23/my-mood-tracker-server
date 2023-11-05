package com.niksob.domain.exception.user.data.access;

import com.niksob.domain.model.user.Username;

public class IllegalUserAccessException extends RuntimeException {
    private final Username username;

    public IllegalUserAccessException(Username username) {
        super();
        this.username = username;
    }

    public IllegalUserAccessException(Username username, String message) {
        super(message);
        this.username = username;
    }

    public IllegalUserAccessException(Username username, String message, Throwable cause) {
        super(message, cause);
        this.username = username;
    }

    public IllegalUserAccessException(Username username, Throwable cause) {
        super(cause);
        this.username = username;
    }

    protected IllegalUserAccessException(
            Username username,
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.username = username;
    }
}
