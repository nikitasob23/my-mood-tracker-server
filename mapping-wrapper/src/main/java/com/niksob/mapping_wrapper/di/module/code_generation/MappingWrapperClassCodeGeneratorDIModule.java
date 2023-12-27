package com.niksob.mapping_wrapper.di.module.code_generation;

import com.niksob.mapping_wrapper.di.module.code_generation.builder.MappingWrapperClassCodeBuilderDIModule;
import com.niksob.mapping_wrapper.service.code_generation.class_code.MappingWrapperCodeGenerator;
import com.niksob.mapping_wrapper.service.code_generation.class_code.MappingWrapperCodeGeneratorImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperClassCodeGeneratorDIModule {
    private final MappingWrapperClassCodeBuilderDIModule mappingWrapperClassCodeBuilderDIModule;

    public MappingWrapperCodeGenerator provide() {
        return new MappingWrapperCodeGeneratorImpl(mappingWrapperClassCodeBuilderDIModule.provide());
    }
}
