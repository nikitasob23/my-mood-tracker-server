package com.niksob.layer_connector.di.module.code_generation;

import com.niksob.layer_connector.di.module.code_generation.builder.MapperWrapperMethodCodeBuilderDIModule;
import com.niksob.layer_connector.di.module.mapper.MappingMethodDetailsMapperDIModule;
import com.niksob.layer_connector.di.module.util.ClassUtilDIModule;
import com.niksob.layer_connector.service.generation.code.layer_connector.method.LayerConnectorMethodCodeGenerator;
import com.niksob.layer_connector.service.generation.code.layer_connector.method.LayerConnectorMethodCodeGeneratorImpl;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MappingWrapperMethodCodeGeneratorDIModule {
    private final MapperWrapperMethodCodeBuilderDIModule mapperWrapperMethodCodeBuilderDIModule;
    private final ClassUtilDIModule classUtilDIModule;
    private final MappingMethodDetailsMapperDIModule mappingMethodDetailsMapperDIModule;
    public LayerConnectorMethodCodeGenerator provide() {
        return new LayerConnectorMethodCodeGeneratorImpl(
                mapperWrapperMethodCodeBuilderDIModule.provide(),
                classUtilDIModule.provide(),
                mappingMethodDetailsMapperDIModule.provide()
        );
    }
}
