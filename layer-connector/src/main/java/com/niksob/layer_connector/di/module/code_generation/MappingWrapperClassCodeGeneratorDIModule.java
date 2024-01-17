package com.niksob.layer_connector.di.module.code_generation;

import com.niksob.layer_connector.di.module.code_generation.builder.MappingWrapperClassCodeBuilderDIModule;
import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.LayerConnectorCodeGenerator;
import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.LayerConnectorCodeGeneratorImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperClassCodeGeneratorDIModule {
    private final MappingWrapperClassCodeBuilderDIModule mappingWrapperClassCodeBuilderDIModule;

    public LayerConnectorCodeGenerator provide() {
        return new LayerConnectorCodeGeneratorImpl(mappingWrapperClassCodeBuilderDIModule.provide());
    }
}
