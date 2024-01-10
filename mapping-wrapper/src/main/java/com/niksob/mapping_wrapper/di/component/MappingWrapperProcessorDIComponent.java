package com.niksob.mapping_wrapper.di.component;

import com.niksob.mapping_wrapper.di.module.*;
import com.niksob.mapping_wrapper.di.module.code_generation.MappingWrapperClassCodeGeneratorDIModule;
import com.niksob.mapping_wrapper.di.module.code_generation.MappingWrapperMethodCodeGeneratorDIModule;
import com.niksob.mapping_wrapper.di.module.code_generation.builder.MapperWrapperMethodCodeBuilderDIModule;
import com.niksob.mapping_wrapper.di.module.code_generation.builder.MappingWrapperClassCodeBuilderDIModule;
import com.niksob.mapping_wrapper.di.module.code_generation.builder.string_builder.MappingWrapperCodeStringBuilderDIModule;
import com.niksob.mapping_wrapper.di.module.logger.LoggerDIModule;
import com.niksob.mapping_wrapper.di.module.service.MappingWrapperServiceDIModule;
import com.niksob.mapping_wrapper.di.module.service.annotation.MappingWrapperAnnotationServiceDIModule;
import com.niksob.mapping_wrapper.di.module.service.element.ElementMethodServiceDIModule;
import com.niksob.mapping_wrapper.di.module.util.ClassUtilDIModule;
import com.niksob.mapping_wrapper.processor.MappingWrapperProcessor;
import lombok.Builder;

@Builder
public class MappingWrapperProcessorDIComponent {
    private MappingWrapperClassCodeGeneratorDIModule mappingWrapperClassCodeGeneratorDIModule;
    @Builder.Default
    private ClassUtilDIModule classUtilDIModule = new ClassUtilDIModule();
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
            MappingWrapperCodeStringBuilderDIModule mappingWrapperCodeStringBuilderDIModule,
            MappingWrapperProcessorEnableDIModule mappingWrapperProcessorEnableDIModule,
            LoggerDIModule loggerDIModule,
            MappingWrapperServiceDIModule mappingWrapperServiceDIModule
    ) {
        this.classUtilDIModule = classUtilDIModule;
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

    public void inject(MappingWrapperProcessor mappingWrapperProcessor) {

        mappingWrapperProcessor.setMappingWrapperService(mappingWrapperServiceDIModule.provide())
                .setClassUtil(classUtilDIModule.provide())
                .setMappingWrapperCodeGenerator(mappingWrapperClassCodeGeneratorDIModule.provide())
                .setProcessorEnable(mappingWrapperProcessorEnableDIModule.provide());
    }


    private MappingWrapperServiceDIModule setMappingWrapperServiceDIModule() {
        var mappingWrapperAnnotationServiceDIModule = new MappingWrapperAnnotationServiceDIModule(loggerDIModule);
        var elementMethodServiceDIModule = new ElementMethodServiceDIModule(loggerDIModule);
        return new MappingWrapperServiceDIModule(
                mappingWrapperAnnotationServiceDIModule, elementMethodServiceDIModule
        );
    }

    private MappingWrapperClassCodeGeneratorDIModule setMappingWrapperClassCodeGeneratorDIModule() {
        var mapperWrapperMethodCodeBuilderDIModule = new MapperWrapperMethodCodeBuilderDIModule(classUtilDIModule);
        var mappingWrapperMethodCodeGeneratorDIModule =
                new MappingWrapperMethodCodeGeneratorDIModule(
                        mapperWrapperMethodCodeBuilderDIModule, classUtilDIModule);
        var mappingWrapperClassCodeBuilderDIModule = new MappingWrapperClassCodeBuilderDIModule(
                mappingWrapperMethodCodeGeneratorDIModule, classUtilDIModule, mappingWrapperCodeStringBuilderDIModule
        );
        return new MappingWrapperClassCodeGeneratorDIModule(mappingWrapperClassCodeBuilderDIModule);
    }
}

