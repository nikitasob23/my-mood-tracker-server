package com.niksob.mapping_wrapper.di.module.logger;

import com.niksob.mapping_wrapper.logger.Logger;
import com.niksob.mapping_wrapper.logger.ProcessorLogger;
import lombok.AllArgsConstructor;

import javax.annotation.processing.ProcessingEnvironment;

@AllArgsConstructor
public class LoggerDIModule {
    private ProcessingEnvironment processingEnv;

    public Logger provide() {
        return new ProcessorLogger(processingEnv);
    }
}
