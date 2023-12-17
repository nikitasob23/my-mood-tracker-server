package com.niksob.mapping_wrapper.di.module;

import com.niksob.mapping_wrapper.service.annotation.mapping_wrapper.MappingWrapperAnnotationService;
import com.niksob.mapping_wrapper.service.annotation.mapping_wrapper.MappingWrapperAnnotationServiceImpl;
import com.niksob.mapping_wrapper.service.element.ElementMethodService;
import com.niksob.mapping_wrapper.service.element.ElementMethodServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperProcessorDIModule {
    private LoggerDIModule loggerDIModule;

    public MappingWrapperAnnotationService provideMappingWrapperService() {
        return new MappingWrapperAnnotationServiceImpl(loggerDIModule.getProcessorLogger());
    }

    public ElementMethodService provideElementMethodService() {
        return new ElementMethodServiceImpl(loggerDIModule.getProcessorLogger());
    }
}
