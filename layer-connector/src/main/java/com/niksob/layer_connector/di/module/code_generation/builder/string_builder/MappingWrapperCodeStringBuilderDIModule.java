package com.niksob.layer_connector.di.module.code_generation.builder.string_builder;

import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.builder.string.LayerConnectorCodeStringBuilder;
import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.builder.string.LayerConnectorCodeStringBuilderImpl;

public class MappingWrapperCodeStringBuilderDIModule {
    public LayerConnectorCodeStringBuilder provide() {
        return new LayerConnectorCodeStringBuilderImpl();
    }
}
