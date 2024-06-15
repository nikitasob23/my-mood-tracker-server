package com.niksob.layer_connector.di.component;

import com.niksob.layer_connector.di.module.*;
import com.niksob.layer_connector.di.module.code_generation.MappingWrapperClassCodeGeneratorDIModule;
import com.niksob.layer_connector.di.module.code_generation.MappingWrapperMethodCodeGeneratorDIModule;
import com.niksob.layer_connector.di.module.code_generation.builder.MapperWrapperMethodCodeBuilderDIModule;
import com.niksob.layer_connector.di.module.code_generation.builder.MappingWrapperClassCodeBuilderDIModule;
import com.niksob.layer_connector.di.module.code_generation.builder.string_builder.MappingWrapperCodeStringBuilderDIModule;
import com.niksob.layer_connector.di.module.logger.LoggerDIModule;
import com.niksob.layer_connector.di.module.mapper.MappingMethodDetailsMapperDIModule;
import com.niksob.layer_connector.di.module.service.MappingWrapperServiceDIModule;
import com.niksob.layer_connector.di.module.service.annotation.MappingWrapperAnnotationServiceDIModule;
import com.niksob.layer_connector.di.module.service.element.ElementMethodServiceDIModule;
import com.niksob.layer_connector.di.module.util.ClassUtilDIModule;
import com.niksob.layer_connector.di.module.util.DateUtilDIModule;
import com.niksob.layer_connector.processor.LayerConnectorProcessor;
import lombok.Builder;

@Builder
public class MappingWrapperProcessorDIComponent {
    private MappingWrapperClassCodeGeneratorDIModule mappingWrapperClassCodeGeneratorDIModule;
    @Builder.Default
    private ClassUtilDIModule classUtilDIModule = new ClassUtilDIModule();
    @Builder.Default
    private DateUtilDIModule dateUtilDIModule = new DateUtilDIModule();
    @Builder.Default
    private MappingWrapperCodeStringBuilderDIModule mappingWrapperCodeStringBuilderDIModule =
            new MappingWrapperCodeStringBuilderDIModule();
    @Builder.Default
    private MappingWrapperProcessorEnableDIModule mappingWrapperProcessorEnableDIModule =
            new MappingWrapperProcessorEnableDIModule();
    private LoggerDIModule loggerDIModule;
    private MappingWrapperServiceDIModule mappingWrapperServiceDIModule;

    public MappingWrapperProcessorDIComponent(
            MappingWrapperClassCodeGeneratorDIModule mappingWrapperClassCodeGeneratorDIModule,
            ClassUtilDIModule classUtilDIModule,
            DateUtilDIModule dateUtilDIModule,
            MappingWrapperCodeStringBuilderDIModule mappingWrapperCodeStringBuilderDIModule,
            MappingWrapperProcessorEnableDIModule mappingWrapperProcessorEnableDIModule,
            LoggerDIModule loggerDIModule,
            MappingWrapperServiceDIModule mappingWrapperServiceDIModule
    ) {
        this.classUtilDIModule = classUtilDIModule;
        this.dateUtilDIModule = dateUtilDIModule;
        this.mappingWrapperCodeStringBuilderDIModule = mappingWrapperCodeStringBuilderDIModule;
        this.mappingWrapperProcessorEnableDIModule = mappingWrapperProcessorEnableDIModule;
        this.mappingWrapperClassCodeGeneratorDIModule = mappingWrapperClassCodeGeneratorDIModule == null
                ? setMappingWrapperClassCodeGeneratorDIModule() : mappingWrapperClassCodeGeneratorDIModule;
        if (loggerDIModule == null) {
            throw new IllegalArgumentException("loggerDIModule must be initialized");
        }
        this.loggerDIModule = loggerDIModule;
        this.mappingWrapperServiceDIModule = mappingWrapperServiceDIModule == null
                ? setMappingWrapperServiceDIModule() : mappingWrapperServiceDIModule;
    }

    public void inject(LayerConnectorProcessor layerConnectorProcessor) {

        layerConnectorProcessor.setLayerConnectorService(mappingWrapperServiceDIModule.provide())
                .setClassUtil(classUtilDIModule.provide())
                .setLayerConnectorCodeGenerator(mappingWrapperClassCodeGeneratorDIModule.provide())
                .setProcessorEnable(mappingWrapperProcessorEnableDIModule.provide());
    }


    private MappingWrapperServiceDIModule setMappingWrapperServiceDIModule() {
        var mappingWrapperAnnotationServiceDIModule = new MappingWrapperAnnotationServiceDIModule(loggerDIModule);
        var elementMethodServiceDIModule = new ElementMethodServiceDIModule(loggerDIModule);
        return new MappingWrapperServiceDIModule(
                mappingWrapperAnnotationServiceDIModule, elementMethodServiceDIModule, dateUtilDIModule, loggerDIModule
        );
    }

    private MappingWrapperClassCodeGeneratorDIModule setMappingWrapperClassCodeGeneratorDIModule() {
        var mapperWrapperMethodCodeBuilderDIModule = new MapperWrapperMethodCodeBuilderDIModule(classUtilDIModule);
        var mappingMethodDetailsMapperDIModule = new MappingMethodDetailsMapperDIModule();
        var mappingWrapperMethodCodeGeneratorDIModule =
                new MappingWrapperMethodCodeGeneratorDIModule(
                        mapperWrapperMethodCodeBuilderDIModule, classUtilDIModule, mappingMethodDetailsMapperDIModule);
        var mappingWrapperClassCodeBuilderDIModule = new MappingWrapperClassCodeBuilderDIModule(
                mappingWrapperMethodCodeGeneratorDIModule, classUtilDIModule, mappingWrapperCodeStringBuilderDIModule
        );
        return new MappingWrapperClassCodeGeneratorDIModule(mappingWrapperClassCodeBuilderDIModule);
    }
}

