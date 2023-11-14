package com.niksob.logger.masked.factory;

import com.niksob.logger.masked.MaskedMessageLogger;
import com.niksob.logger.service.masking.MaskingStringFieldService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MaskedLoggerFactory implements ILoggerFactory {

    private static MaskingStringFieldService maskingStringFieldService;

    public MaskedLoggerFactory(MaskingStringFieldService maskingStringFieldService) {
        MaskedLoggerFactory.maskingStringFieldService = maskingStringFieldService;
    }

    public static Logger create(Class<?> clazz) {
        throwIfMaskingServiceIsNull();
        final org.slf4j.Logger logger = LoggerFactory.getLogger(clazz);
        return new MaskedMessageLogger(logger, maskingStringFieldService);
    }

    @Override
    public Logger getLogger(String s) {
        throwIfMaskingServiceIsNull();
        final Logger logger = LoggerFactory.getLogger(s);
        return new MaskedMessageLogger(logger, maskingStringFieldService);
    }

    private static void throwIfMaskingServiceIsNull() {
        if (maskingStringFieldService == null) {
            throw new IllegalStateException("Static logger tries to be created when app context is not yet been initialized");
        }
    }
}
