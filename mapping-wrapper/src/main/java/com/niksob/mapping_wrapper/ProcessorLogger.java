package com.niksob.mapping_wrapper;

import lombok.AllArgsConstructor;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

@AllArgsConstructor
public class ProcessorLogger implements Logger {
    private ProcessingEnvironment processingEnv;

    @Override
    public void warn(String s) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.WARNING, s);
    }
}
