package com.niksob.layer_connector.di.module.code_generation.builder;

import com.niksob.layer_connector.di.module.code_generation.MappingWrapperMethodCodeGeneratorDIModule;
import com.niksob.layer_connector.di.module.code_generation.builder.string_builder.MappingWrapperCodeStringBuilderDIModule;
import com.niksob.layer_connector.di.module.util.ClassUtilDIModule;
import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.builder.LayerConnectorClassCodeBuilder;
import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.builder.LayerConnectorClassCodeBuilderImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperClassCodeBuilderDIModule {
    private final MappingWrapperMethodCodeGeneratorDIModule mappingWrapperMethodCodeGeneratorDIModule;
    private final ClassUtilDIModule classUtilDIModule;
    private final MappingWrapperCodeStringBuilderDIModule mappingWrapperCodeStringBuilderDIModule;

    public LayerConnectorClassCodeBuilder provide() {
        return new LayerConnectorClassCodeBuilderImpl(
                mappingWrapperMethodCodeGeneratorDIModule.provide(),
                classUtilDIModule.provide(),
                mappingWrapperCodeStringBuilderDIModule.provide()
        );
    }
}
