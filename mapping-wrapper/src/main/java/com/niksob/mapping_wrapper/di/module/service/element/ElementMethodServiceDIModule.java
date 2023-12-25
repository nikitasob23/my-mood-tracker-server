package com.niksob.mapping_wrapper.di.module.service.element;

import com.niksob.mapping_wrapper.di.module.logger.LoggerDIModule;
import com.niksob.mapping_wrapper.service.element.ElementMethodService;
import com.niksob.mapping_wrapper.service.element.ElementMethodServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ElementMethodServiceDIModule {
    private final LoggerDIModule loggerDIModule;

    public ElementMethodService provide() {
        return new ElementMethodServiceImpl(loggerDIModule.provide());
    }
}
