package com.niksob.layer_connector.di.module.code_generation.builder;

import com.niksob.layer_connector.di.module.util.ClassUtilDIModule;
import com.niksob.layer_connector.service.generation.code.layer_connector.method.builder.LayerConnectorMethodCodeBuilder;
import com.niksob.layer_connector.service.generation.code.layer_connector.method.builder.LayerConnectorMethodCodeBuilderImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MapperWrapperMethodCodeBuilderDIModule {
    private final ClassUtilDIModule classUtilDIModule;

    public LayerConnectorMethodCodeBuilder provide() {
        return new LayerConnectorMethodCodeBuilderImpl(classUtilDIModule.provide());
    }
}
