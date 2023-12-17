package com.niksob.mapping_wrapper.di.component;

import com.niksob.mapping_wrapper.di.module.LoggerDIModule;
import com.niksob.mapping_wrapper.di.module.MappingWrapperImplClassCodeServiceDIModule;
import com.niksob.mapping_wrapper.di.module.MappingWrapperProcessorDIModule;
import com.niksob.mapping_wrapper.di.module.MappingWrapperProcessorEnableDIModule;
import com.niksob.mapping_wrapper.processor.MappingWrapperProcessor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class MappingWrapperProcessorDIComponent {
    private MappingWrapperImplClassCodeServiceDIModule mappingWrapperImplClassCodeServiceDIModule;
    private MappingWrapperProcessorDIModule mappingWrapperProcessorDIModule;
    private LoggerDIModule loggerDIModule;
    private MappingWrapperProcessorEnableDIModule mappingWrapperProcessorEnableDIModule;

    public void inject(MappingWrapperProcessor mappingWrapperProcessor) {
        mappingWrapperImplClassCodeServiceDIModule = mappingWrapperImplClassCodeServiceDIModule == null ?
                new MappingWrapperImplClassCodeServiceDIModule() : mappingWrapperImplClassCodeServiceDIModule;
        mappingWrapperProcessorDIModule = mappingWrapperProcessorDIModule == null ?
                new MappingWrapperProcessorDIModule(loggerDIModule) : mappingWrapperProcessorDIModule;
        mappingWrapperProcessorEnableDIModule = mappingWrapperProcessorEnableDIModule == null ?
                new MappingWrapperProcessorEnableDIModule() : mappingWrapperProcessorEnableDIModule;

        mappingWrapperProcessor.setMappingWrapperImplClassCodeService(
                        mappingWrapperImplClassCodeServiceDIModule.provide()
                )
                .setMappingWrapperService(mappingWrapperProcessorDIModule.provideMappingWrapperService())
                .setElementMethodService(mappingWrapperProcessorDIModule.provideElementMethodService())
                .setProcessorEnable(mappingWrapperProcessorEnableDIModule.provide());
    }
}
