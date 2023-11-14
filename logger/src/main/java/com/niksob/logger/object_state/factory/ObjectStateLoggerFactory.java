package com.niksob.logger.object_state.factory;

import com.niksob.logger.mapper.json.AppJsonMapper;
import com.niksob.logger.object_state.ObjectStateLogger;
import com.niksob.logger.object_state.ObjectStateLoggerImpl;
import com.niksob.logger.service.masking.MaskingStringFieldService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ObjectStateLoggerFactory implements ILoggerFactory {
    private static MaskingStringFieldService maskingStringFieldService;
    private static AppJsonMapper jsonMapper;

    public ObjectStateLoggerFactory(MaskingStringFieldService maskingStringFieldService, AppJsonMapper jsonMapper) {
        ObjectStateLoggerFactory.maskingStringFieldService = maskingStringFieldService;
        ObjectStateLoggerFactory.jsonMapper = jsonMapper;
    }

    @Override
    public Logger getLogger(String s) {
        throwIfMaskingServiceIsNull();
        final Logger logger = LoggerFactory.getLogger(s);
        return new ObjectStateLoggerImpl(logger, maskingStringFieldService, jsonMapper);
    }

    public static ObjectStateLogger getLogger(Class<?> clazz) {
        throwIfMaskingServiceIsNull();
        final Logger logger = LoggerFactory.getLogger(clazz);
        return new ObjectStateLoggerImpl(logger, maskingStringFieldService, jsonMapper);
    }

    private static void throwIfMaskingServiceIsNull() {
        if (maskingStringFieldService == null && jsonMapper == null) {
            throw new IllegalStateException("Static logger tries to be created when app context is not yet been initialized");
        }
    }
}
