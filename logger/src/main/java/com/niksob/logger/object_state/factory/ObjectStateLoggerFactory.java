package com.niksob.logger.object_state.factory;

import com.niksob.logger.mapper.json.MaskedAppJsonMapper;
import com.niksob.logger.object_state.AppLogger;
import com.niksob.logger.object_state.ObjectStateAppLoggerImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ObjectStateLoggerFactory {

    private static MaskedAppJsonMapper jsonMapper;

    public ObjectStateLoggerFactory(@Qualifier("app_json_mapper") MaskedAppJsonMapper jsonMapper) {
        ObjectStateLoggerFactory.jsonMapper = jsonMapper;
    }

    public static AppLogger create(Class<?> clazz) {
        if (jsonMapper == null) {
            throw new IllegalStateException("Static logger tries to be created when app context is not yet been initialized");
        }
        final org.slf4j.Logger logger = LoggerFactory.getLogger(clazz);
        return new ObjectStateAppLoggerImpl(logger, jsonMapper);
    }
}
