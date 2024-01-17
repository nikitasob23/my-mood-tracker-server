package com.niksob.layer_connector.di.module.service.annotation;

import com.niksob.layer_connector.di.module.logger.LoggerDIModule;
import com.niksob.layer_connector.service.annotation.layer_connector.LayerConnectorAnnotationService;
import com.niksob.layer_connector.service.annotation.layer_connector.LayerConnectorAnnotationServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperAnnotationServiceDIModule {
    private final LoggerDIModule loggerDIModule;

    public LayerConnectorAnnotationService provide() {
        return new LayerConnectorAnnotationServiceImpl(loggerDIModule.provide());
    }
}
