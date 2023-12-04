package com.niksob.database_service.exception.entity;

import com.niksob.domain.model.user.UserInfo;

public class EntityNotDeletedException extends RuntimeException {
    private final UserInfo userInfo;
    public EntityNotDeletedException(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public EntityNotDeletedException(String message, UserInfo userInfo) {
        super(message);
        this.userInfo = userInfo;
    }

    public EntityNotDeletedException(String message, UserInfo userInfo, Throwable cause) {
        super(message, cause);
        this.userInfo = userInfo;
    }

    public EntityNotDeletedException(UserInfo userInfo, Throwable cause) {
        super(cause);
        this.userInfo = userInfo;
    }

    public EntityNotDeletedException(
            String message,
            UserInfo userInfo,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.userInfo = userInfo;
    }
}
