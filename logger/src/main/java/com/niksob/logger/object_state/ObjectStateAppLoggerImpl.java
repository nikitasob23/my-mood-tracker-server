package com.niksob.logger.object_state;

import com.niksob.logger.mapper.json.AppJsonMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;

@RequiredArgsConstructor
public class ObjectStateAppLoggerImpl implements AppLogger {

    public static final String OBJECT_STATE_KEY = "objectState";

    private final org.slf4j.Logger log;

    private final AppJsonMapper jsonMapper;

    @Override
    public void debug(String message, Throwable throwable, Object objectState) {
        objectStateLogging(() -> log.debug(message, throwable), objectState);
    }

    @Override
    public void info(String message, Throwable throwable, Object objectState) {
        objectStateLogging(() -> log.info(message, throwable), objectState);
    }

    @Override
    public void error(String message, Throwable throwable, Object objectState) {
        objectStateLogging(() -> log.error(message, throwable), objectState);
    }

    private <T> void objectStateLogging(Runnable logging, T objectState) {
        MDC.put(OBJECT_STATE_KEY, jsonMapper.toJson(objectState));
        logging.run();
        MDC.remove(OBJECT_STATE_KEY);
    }
}
