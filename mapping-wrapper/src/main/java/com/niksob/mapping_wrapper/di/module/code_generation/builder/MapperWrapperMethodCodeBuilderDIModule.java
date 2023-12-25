package com.niksob.mapping_wrapper.di.module.code_generation.builder;

import com.niksob.mapping_wrapper.di.module.util.ClassUtilDIModule;
import com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.builder.MapperWrapperMethodCodeBuilder;
import com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.builder.MapperWrapperMethodCodeBuilderImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MapperWrapperMethodCodeBuilderDIModule {
    private final ClassUtilDIModule classUtilDIModule;

    public MapperWrapperMethodCodeBuilder provide() {
        return new MapperWrapperMethodCodeBuilderImpl(classUtilDIModule.provide());
    }
}
