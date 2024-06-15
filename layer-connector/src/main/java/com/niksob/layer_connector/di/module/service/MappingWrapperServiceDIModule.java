package com.niksob.layer_connector.di.module.service;

import com.niksob.layer_connector.di.module.logger.LoggerDIModule;
import com.niksob.layer_connector.di.module.service.annotation.MappingWrapperAnnotationServiceDIModule;
import com.niksob.layer_connector.di.module.service.element.ElementMethodServiceDIModule;
import com.niksob.layer_connector.di.module.util.DateUtilDIModule;
import com.niksob.layer_connector.service.class_element.LayerConnectorService;
import com.niksob.layer_connector.service.class_element.LayerConnectorServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperServiceDIModule {
    private final MappingWrapperAnnotationServiceDIModule mappingWrapperAnnotationServiceDIModule;
    private final ElementMethodServiceDIModule elementMethodServiceDIModule;
    private final DateUtilDIModule dateUtilDIModule;
    private final LoggerDIModule loggerDIModule;

    public LayerConnectorService provide() {
        return new LayerConnectorServiceImpl(
                mappingWrapperAnnotationServiceDIModule.provide(),
                elementMethodServiceDIModule.provide(),
                dateUtilDIModule.provide(),
                loggerDIModule.provide()
        );
    }
}
