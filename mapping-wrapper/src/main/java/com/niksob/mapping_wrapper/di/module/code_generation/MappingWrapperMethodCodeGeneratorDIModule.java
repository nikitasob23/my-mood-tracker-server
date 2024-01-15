package com.niksob.mapping_wrapper.di.module.code_generation;

import com.niksob.mapping_wrapper.di.module.code_generation.builder.MapperWrapperMethodCodeBuilderDIModule;
import com.niksob.mapping_wrapper.di.module.mapper.MappingMethodDetailsMapperDIModule;
import com.niksob.mapping_wrapper.di.module.util.ClassUtilDIModule;
import com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.MappingWrapperMethodCodeGenerator;
import com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.MappingWrapperMethodCodeGeneratorImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperMethodCodeGeneratorDIModule {
    private final MapperWrapperMethodCodeBuilderDIModule mapperWrapperMethodCodeBuilderDIModule;
    private final ClassUtilDIModule classUtilDIModule;
    private final MappingMethodDetailsMapperDIModule mappingMethodDetailsMapperDIModule;
    public MappingWrapperMethodCodeGenerator provide() {
        return new MappingWrapperMethodCodeGeneratorImpl(
                mapperWrapperMethodCodeBuilderDIModule.provide(),
                classUtilDIModule.provide(),
                mappingMethodDetailsMapperDIModule.provide()
        );
    }
}
