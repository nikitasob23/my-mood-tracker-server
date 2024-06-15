package com.niksob.layer_connector.di.module.service.element;

import com.niksob.layer_connector.di.module.logger.LoggerDIModule;
import com.niksob.layer_connector.service.element.ElementMethodService;
import com.niksob.layer_connector.service.element.ElementMethodServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ElementMethodServiceDIModule {
    private final LoggerDIModule loggerDIModule;

    public ElementMethodService provide() {
        return new ElementMethodServiceImpl(loggerDIModule.provide());
    }
}
