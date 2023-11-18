package com.niksob.logger.object_state;

import org.slf4j.Marker;

public interface ObjectStateLogger {
    void trace(String s, Throwable throwable, Object o);

    void trace(String s, Throwable throwable, Object... objects);

    void debug(String s, Throwable throwable, Object o);

    void debug(String s, Throwable throwable, Object... objects);

    void info(String s, Throwable throwable, Object o);

    void info(String s, Throwable throwable, Object... objects);

    void warn(String s, Throwable throwable, Object o);

    void warn(String s, Throwable throwable, Object... objects);

    void error(String s, Throwable throwable, Object o);

    void error(String s, Throwable throwable, Object... objects);
}
