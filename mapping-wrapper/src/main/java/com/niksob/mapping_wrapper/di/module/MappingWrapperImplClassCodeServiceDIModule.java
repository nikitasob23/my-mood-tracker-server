package com.niksob.mapping_wrapper.di.module;

import com.niksob.mapping_wrapper.service.code_builder.class_code.MappingWrapperClassCodeBuilderImpl;
import com.niksob.mapping_wrapper.service.code_builder.method_code.MappingWrapperMethodCodeBuilderImpl;
import com.niksob.mapping_wrapper.service.code_generation.MappingWrapperClassCodeGenerator;
import com.niksob.mapping_wrapper.service.code_generation.MappingWrapperClassCodeGeneratorImpl;

public class MappingWrapperImplClassCodeServiceDIModule {
    public MappingWrapperClassCodeGenerator provide() {
        var methodCodeBuilder = new MappingWrapperMethodCodeBuilderImpl();
        var classCodeBuilder = new MappingWrapperClassCodeBuilderImpl(methodCodeBuilder);
        return new MappingWrapperClassCodeGeneratorImpl(classCodeBuilder);
    }
}
