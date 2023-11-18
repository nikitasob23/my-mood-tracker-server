package com.niksob.logger.masked;

import com.niksob.logger.model.json.Json;
import com.niksob.logger.service.masking.MaskingStringFieldService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MaskedMessageLogger implements Logger {
    protected final org.slf4j.Logger log;
    protected final MaskingStringFieldService maskingStringFieldService;

    private boolean haveKeywordToMask(Object... objects) {
        return Arrays.stream(objects)
                .map(Object::toString)
                .anyMatch(maskingStringFieldService::haveKeywords);
    }

    private Set<String> maskParams(Object... objects) {
        return Arrays.stream(objects)
                .map(Object::toString)
                .map(Json::new)
                .map(maskingStringFieldService::mask)
                .map(Json::value)
                .collect(Collectors.toSet());
    }

    private String insertParams(String str, Set<String> params) {
        for (var param : params) {
            str = str.replaceFirst("\\{\\}", param);
        }
        return str;
    }

    private String format(String str, Object... objects) {
        final Set<String> params = maskParams(objects);
        return insertParams(str, params);
    }

    @Override
    public String getName() {
        return log.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        log.trace(s);
    }

    @Override
    public void trace(String s, Object o) {
        if (haveKeywordToMask(o)) {
            log.trace(format(s, o));
            return;
        }
        log.trace(s, o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        final Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.trace(format(s, objects));
            return;
        }
        log.trace(s, o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.trace(format(s, objects));
            return;
        }
        log.trace(s, objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        log.trace(s, throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return log.isWarnEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        log.trace(marker, s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        if (haveKeywordToMask(o)) {
            log.trace(marker, format(s, o));
            return;
        }
        log.trace(marker, s, o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.trace(marker, format(s, objects));
            return;
        }
        log.trace(marker, s, o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.trace(marker, format(s, objects));
            return;
        }
        log.trace(marker, s, objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        log.trace(marker, s, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        log.debug(s);
    }

    @Override
    public void debug(String s, Object o) {
        if (haveKeywordToMask(o)) {
            log.debug(format(s, o));
            return;
        }
        log.debug(s, o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.debug(format(s, objects));
            return;
        }
        log.debug(s, o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.debug(format(s, objects));
            return;
        }
        log.debug(s, objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        log.debug(s, throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return log.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        log.debug(marker, s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        if (haveKeywordToMask(o)) {
            log.debug(marker, format(s, o));
            return;
        }
        log.debug(marker, s, o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.debug(marker, format(s, objects));
            return;
        }
        log.debug(marker, s, o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.debug(marker, format(s, objects));
            return;
        }
        log.debug(marker, s, objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        log.debug(marker, s, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        log.info(s);
    }

    @Override
    public void info(String s, Object o) {
        if (haveKeywordToMask(o)) {
            log.info(format(s, o));
            return;
        }
        log.info(s, o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.info(format(s, objects));
            return;
        }
        log.info(s, o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.info(format(s, objects));
            return;
        }
        log.info(s, objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        log.info(s, throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return log.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        log.info(marker, s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        if (haveKeywordToMask(o)) {
            log.info(marker, format(s, o));
            return;
        }
        log.info(marker, s, o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.info(marker, format(s, objects));
            return;
        }
        log.info(marker, s, o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.info(format(s, objects));
            return;
        }
        log.info(marker, s, objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        log.info(marker, s, throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }

    @Override
    public void warn(String s, Object o) {
        log.warn(s, o);
    }

    @Override
    public void warn(String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.warn(format(s, objects));
            return;
        }
        log.warn(s, objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.warn(format(s, objects));
            return;
        }
        log.warn(s, o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        log.warn(s, throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return log.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        log.warn(marker, s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        if (haveKeywordToMask(o)) {
            log.warn(marker, format(s, o));
            return;
        }
        log.warn(marker, s, o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.warn(marker, format(s, objects));
            return;
        }
        log.warn(marker, s, o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.warn(marker, format(s, objects));
            return;
        }
        log.warn(marker, s, objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        log.warn(marker, s, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        log.error(s);
    }

    @Override
    public void error(String s, Object o) {
        if (haveKeywordToMask(o)) {
            log.error(format(s, o));
            return;
        }
        log.error(s, o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.error(format(s, objects));
            return;
        }
        log.error(s, o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.error(format(s, objects));
            return;
        }
        log.error(s, objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        log.error(s, throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return log.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String s) {
        log.error(marker, s);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        if (haveKeywordToMask(o)) {
            log.error(marker, format(s, o));
            return;
        }
        log.error(marker, s, o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        Object[] objects = {o, o1};
        if (haveKeywordToMask(objects)) {
            log.error(marker, format(s, objects));
            return;
        }
        log.error(marker, s, o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        if (haveKeywordToMask(objects)) {
            log.error(marker, format(s, objects));
            return;
        }
        log.error(marker, s, objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        log.error(marker, s, throwable);
    }
}
