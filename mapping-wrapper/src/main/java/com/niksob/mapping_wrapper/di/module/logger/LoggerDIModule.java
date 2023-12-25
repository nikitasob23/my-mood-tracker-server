package com.niksob.mapping_wrapper.di.module.logger;

import com.niksob.mapping_wrapper.Logger;
import com.niksob.mapping_wrapper.ProcessorLogger;
import lombok.AllArgsConstructor;

import javax.annotation.processing.ProcessingEnvironment;

@AllArgsConstructor
public class LoggerDIModule {
    private ProcessingEnvironment processingEnv;

    public Logger provide() {
        return new ProcessorLogger(processingEnv);
    }
}
