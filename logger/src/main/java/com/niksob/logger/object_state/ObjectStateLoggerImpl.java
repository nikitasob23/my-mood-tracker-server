package com.niksob.logger.object_state;

import com.niksob.logger.mapper.json.AppJsonMapper;
import com.niksob.logger.masked.MaskedMessageLogger;
import com.niksob.logger.model.json.Json;
import com.niksob.logger.service.masking.MaskingStringFieldService;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.Marker;

public class ObjectStateLoggerImpl extends MaskedMessageLogger implements ObjectStateLogger {
    public static final String OBJECT_STATE_KEY = "objectState";

    private final AppJsonMapper jsonMapper;

    public ObjectStateLoggerImpl(
            Logger log,
            MaskingStringFieldService maskingStringFieldService,
            AppJsonMapper jsonMapper
    ) {
        super(log, maskingStringFieldService);
        this.jsonMapper = jsonMapper;
    }

    // from ObjectStateLogger interface
    @Override
    public void trace(String s, Throwable throwable, Object o) {
        addObjectStatesAndLog(() -> super.log.trace(s, throwable, o), o);
    }

    @Override
    public void trace(String s, Throwable throwable, Object... objects) {
        addObjectStatesAndLog(() -> super.log.trace(s, throwable, objects), objects);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        addObjectStatesAndLog(() -> super.trace(marker, s, o), o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        addObjectStatesAndLog(() -> super.trace(marker, s, o, o1), objects);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        addObjectStatesAndLog(() -> super.trace(marker, s, objects), objects);
    }

    // from ObjectStateLogger interface
    @Override
    public void debug(String s, Throwable throwable, Object o) {
        addObjectStatesAndLog(() -> super.log.debug(s, throwable, o), o);
    }

    @Override
    public void debug(String s, Throwable throwable, Object... objects) {
        addObjectStatesAndLog(() -> super.log.debug(s, throwable, objects), objects);
    }

    @Override
    public void debug(String s, Object o) {
        addObjectStatesAndLog(() -> super.debug(s, o), o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        addObjectStatesAndLog(() -> super.debug(s, o, o1), objects);
    }

    @Override
    public void debug(String s, Object... objects) {
        addObjectStatesAndLog(() -> super.debug(s, objects), objects);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        addObjectStatesAndLog(() -> super.debug(marker, s, o), o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        addObjectStatesAndLog(() -> super.debug(marker, s, o, o1), objects);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        addObjectStatesAndLog(() -> super.debug(marker, s, objects), objects);
    }

    // from ObjectStateLogger interface
    @Override
    public void info(String s, Throwable throwable, Object o) {
        addObjectStatesAndLog(() -> super.log.info(s, throwable, o), o);
    }

    @Override
    public void info(String s, Throwable throwable, Object... objects) {
        addObjectStatesAndLog(() -> super.log.info(s, throwable, objects), objects);
    }

    @Override
    public void info(String s, Object o) {
        addObjectStatesAndLog(() -> super.info(s, o), o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        addObjectStatesAndLog(() -> super.info(s, o, o1), objects);
    }

    @Override
    public void info(String s, Object... objects) {
        addObjectStatesAndLog(() -> super.info(s, objects), objects);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        addObjectStatesAndLog(() -> super.info(marker, s, o), o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        addObjectStatesAndLog(() -> super.info(marker, s, o, o1), objects);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        addObjectStatesAndLog(() -> super.info(marker, s, objects), objects);
    }

    // from ObjectStateLogger interface
    @Override
    public void warn(String s, Throwable throwable, Object o) {
        addObjectStatesAndLog(() -> super.log.warn(s, throwable, o), o);
    }

    @Override
    public void warn(String s, Throwable throwable, Object... objects) {
        addObjectStatesAndLog(() -> super.log.warn(s, throwable, objects), objects);
    }

    @Override
    public void warn(String s, Object o) {
        addObjectStatesAndLog(() -> super.warn(s, o), o);
    }

    @Override
    public void warn(String s, Object... objects) {
        addObjectStatesAndLog(() -> super.warn(s, objects), objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        addObjectStatesAndLog(() -> super.warn(s, o, o1), objects);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        addObjectStatesAndLog(() -> super.warn(marker, s, o), o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        addObjectStatesAndLog(() -> super.warn(marker, s, o, o1), objects);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        addObjectStatesAndLog(() -> super.warn(marker, s, objects), objects);
    }

    // from ObjectStateLogger interface
    @Override
    public void error(String s, Throwable throwable, Object o) {
        addObjectStatesAndLog(() -> super.log.error(s, throwable, o), o);
    }

    @Override
    public void error(String s, Throwable throwable, Object... objects) {
        addObjectStatesAndLog(() -> super.log.error(s, throwable, objects), objects);
    }

    @Override
    public void error(String s, Object o) {
        addObjectStatesAndLog(() -> super.error(s, o), o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        addObjectStatesAndLog(() -> super.error(s, o, o1), objects);
    }

    @Override
    public void error(String s, Object... objects) {
        addObjectStatesAndLog(() -> super.error(s, objects), objects);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        addObjectStatesAndLog(() -> super.error(marker, s, o), o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        addObjectStatesAndLog(() -> super.error(marker, s, o, o1), objects);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        addObjectStatesAndLog(() -> super.error(marker, s, objects), objects);
    }

    private void addObjectStatesAndLog(Runnable logging, Object... objects) {
        MDC.put(OBJECT_STATE_KEY, mask(objects).value());
        logging.run();
        MDC.remove(OBJECT_STATE_KEY);
    }

    private Json mask(Object... objects) {
        final Json json = jsonMapper.toJson(objects);
        if (maskingStringFieldService.haveKeywords(json)) {
            return maskingStringFieldService.mask(json);
        }
        return json;
    }
}
