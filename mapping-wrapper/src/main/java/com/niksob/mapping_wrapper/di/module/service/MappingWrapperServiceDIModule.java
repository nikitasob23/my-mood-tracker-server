package com.niksob.mapping_wrapper.di.module.service;

import com.niksob.mapping_wrapper.di.module.service.annotation.MappingWrapperAnnotationServiceDIModule;
import com.niksob.mapping_wrapper.di.module.service.element.ElementMethodServiceDIModule;
import com.niksob.mapping_wrapper.service.class_element.MappingWrapperService;
import com.niksob.mapping_wrapper.service.class_element.MappingWrapperServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperServiceDIModule {
    private final MappingWrapperAnnotationServiceDIModule mappingWrapperAnnotationServiceDIModule;
    private final ElementMethodServiceDIModule elementMethodServiceDIModule;
    public MappingWrapperService provide() {
        return new MappingWrapperServiceImpl(
                mappingWrapperAnnotationServiceDIModule.provide(),
                elementMethodServiceDIModule.provide()
        );
    }
}
