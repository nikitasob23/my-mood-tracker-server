package com.niksob.mapping_wrapper.di.module.code_generation;

import com.niksob.mapping_wrapper.di.module.code_generation.builder.MappingWrapperClassCodeBuilderDIModule;
import com.niksob.mapping_wrapper.service.code_generation.class_code.GenerateMappingWrapperCodeService;
import com.niksob.mapping_wrapper.service.code_generation.class_code.GenerateMappingWrapperCodeServiceImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperClassCodeGeneratorDIModule {
    private final MappingWrapperClassCodeBuilderDIModule mappingWrapperClassCodeBuilderDIModule;

    public GenerateMappingWrapperCodeService provide() {
        return new GenerateMappingWrapperCodeServiceImpl(mappingWrapperClassCodeBuilderDIModule.provide());
    }
}
