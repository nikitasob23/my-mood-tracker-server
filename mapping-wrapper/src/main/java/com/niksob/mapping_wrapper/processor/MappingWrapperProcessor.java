package com.niksob.mapping_wrapper.processor;

import com.google.auto.service.AutoService;
import com.niksob.mapping_wrapper.di.component.MappingWrapperProcessorDIComponent;
import com.niksob.mapping_wrapper.di.module.logger.LoggerDIModule;
import com.niksob.mapping_wrapper.service.class_element.MappingWrapperService;
import com.niksob.mapping_wrapper.service.code_generation.class_code.GenerateMappingWrapperCodeService;
import com.niksob.mapping_wrapper.util.ClassUtil;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("com.niksob.mapping_wrapper.processor.MappingWrapper")
@Setter
@Accessors(chain = true)
public class MappingWrapperProcessor extends AbstractProcessor {
    private GenerateMappingWrapperCodeService generateMappingWrapperCodeService;
    private MappingWrapperService mappingWrapperService;
    private ClassUtil classUtil;

    private boolean processorEnable;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        MappingWrapperProcessorDIComponent.builder()
                .loggerDIModule(new LoggerDIModule(processingEnv))
                .build()
                .inject(this);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!processorEnable) {
            return false;
        }
        for (var element : annotations) {
            roundEnv.getElementsAnnotatedWith(element)
                    .forEach(this::generateMappingWrapperClass);
        }
        return true;
    }

    private void generateMappingWrapperClass(Element e) {
        var mappingWrapperClassDetails = mappingWrapperService.extractClassDetails(e);
        var classCode = generateMappingWrapperCodeService.generateClassCode(mappingWrapperClassDetails);
        var mappingWrapperName = classUtil.stickNames(
                mappingWrapperClassDetails.getInterfaceDetails().getName(),
                MappingWrapperService.MAPPING_WRAPPER_NAME_POSTFIX
        );
        createMappingWrapperImplementationClass(mappingWrapperName, classCode);
    }

    private void createMappingWrapperImplementationClass(String implementationFullName, String s) {
        try (PrintWriter writer = new PrintWriter(
                processingEnv.getFiler().createSourceFile(implementationFullName).openWriter())
        ) {
            writer.println(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
