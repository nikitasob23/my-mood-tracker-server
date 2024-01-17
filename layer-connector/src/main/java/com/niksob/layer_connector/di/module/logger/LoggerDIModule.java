package com.niksob.layer_connector.di.module.logger;

import com.niksob.layer_connector.logger.Logger;
import com.niksob.layer_connector.logger.ProcessorLogger;
import lombok.AllArgsConstructor;

import javax.annotation.processing.ProcessingEnvironment;

@AllArgsConstructor
public class LoggerDIModule {
    private ProcessingEnvironment processingEnv;

    public Logger provide() {
        return new ProcessorLogger(processingEnv);
    }
}
