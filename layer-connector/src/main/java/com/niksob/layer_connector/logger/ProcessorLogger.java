package com.niksob.layer_connector.logger;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

@Component
@AllArgsConstructor
public class ProcessorLogger implements Logger {
    private ProcessingEnvironment processingEnv;

    @Override
    public void warn(String s) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.WARNING, s);
    }
}
