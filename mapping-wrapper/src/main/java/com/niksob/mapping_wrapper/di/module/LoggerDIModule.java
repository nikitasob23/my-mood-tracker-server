package com.niksob.mapping_wrapper.di.module;

import com.niksob.mapping_wrapper.Logger;
import com.niksob.mapping_wrapper.ProcessorLogger;
import lombok.AllArgsConstructor;

import javax.annotation.processing.ProcessingEnvironment;

@AllArgsConstructor
public class LoggerDIModule {
    private final ProcessingEnvironment processingEnv;

    public Logger getProcessorLogger() {
        return new ProcessorLogger(processingEnv);
    }
}
