package com.niksob.mapping_wrapper.di.module.service.annotation;

import com.niksob.mapping_wrapper.di.module.logger.LoggerDIModule;
import com.niksob.mapping_wrapper.service.annotation.mapping_wrapper.MappingWrapperAnnotationService;
import com.niksob.mapping_wrapper.service.annotation.mapping_wrapper.MappingWrapperAnnotationServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperAnnotationServiceDIModule {
    private final LoggerDIModule loggerDIModule;

    public MappingWrapperAnnotationService provide() {
        return new MappingWrapperAnnotationServiceImpl(loggerDIModule.provide());
    }
}
