package com.niksob.mapping_wrapper.di.module.code_generation.builder.string_builder;

import com.niksob.mapping_wrapper.service.code_generation.class_code.builder.string_builder.MappingWrapperCodeStringBuilder;
import com.niksob.mapping_wrapper.service.code_generation.class_code.builder.string_builder.MappingWrapperCodeStringBuilderImpl;

public class MappingWrapperCodeStringBuilderDIModule {
    public MappingWrapperCodeStringBuilder provide() {
        return new MappingWrapperCodeStringBuilderImpl();
    }
}
