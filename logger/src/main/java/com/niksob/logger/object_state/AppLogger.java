package com.niksob.logger.object_state;

import com.niksob.domain.exception.user.data.access.IllegalUserAccessException;
import com.niksob.domain.model.user.UserInfo;
import com.niksob.domain.model.user.Username;

public interface AppLogger {
    void debug(String message, Throwable throwable, Object objectState);

    void info(String message, Throwable throwable, Object objectState);

    void error(String message, Throwable throwable, Object objectState);
}
