package com.niksob.mapping_wrapper.di.module.code_generation.builder;

import com.niksob.mapping_wrapper.di.module.code_generation.MappingWrapperMethodCodeGeneratorDIModule;
import com.niksob.mapping_wrapper.di.module.util.ClassUtilDIModule;
import com.niksob.mapping_wrapper.service.code_generation.class_code.builder.MappingWrapperClassCodeBuilder;
import com.niksob.mapping_wrapper.service.code_generation.class_code.builder.MappingWrapperClassCodeBuilderImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperClassCodeBuilderDIModule {
    private final MappingWrapperMethodCodeGeneratorDIModule mappingWrapperMethodCodeGeneratorDIModule;
    private final ClassUtilDIModule classUtilDIModule;

    public MappingWrapperClassCodeBuilder provide() {
        return new MappingWrapperClassCodeBuilderImpl(
                mappingWrapperMethodCodeGeneratorDIModule.provide(),
                classUtilDIModule.provide()
        );
    }
}
