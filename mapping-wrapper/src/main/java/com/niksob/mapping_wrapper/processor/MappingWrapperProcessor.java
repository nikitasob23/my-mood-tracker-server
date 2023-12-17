package com.niksob.mapping_wrapper.processor;

import com.google.auto.service.AutoService;
import com.niksob.mapping_wrapper.di.component.MappingWrapperProcessorDIComponent;
import com.niksob.mapping_wrapper.di.module.LoggerDIModule;
import com.niksob.mapping_wrapper.model.mapping_wrapper.marker.Marker;
import com.niksob.mapping_wrapper.model.mapping_wrapper.*;
import com.niksob.mapping_wrapper.service.annotation.mapping_wrapper.MappingWrapperAnnotationService;
import com.niksob.mapping_wrapper.service.code_generation.MappingWrapperClassCodeGenerator;
import com.niksob.mapping_wrapper.service.element.ElementMethodService;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Stream;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("com.niksob.mapping_wrapper.processor.MappingWrapper")
@Setter
@Accessors(chain = true)
public class MappingWrapperProcessor extends AbstractProcessor {
    private MappingWrapperClassCodeGenerator mappingWrapperImplClassCodeService;
    private MappingWrapperAnnotationService mappingWrapperService;
    private ElementMethodService elementMethodService;

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
        var annotationDetails = mappingWrapperService.extractAnnotationDetails(e);
        var interfaceMethodSignatures =
                elementMethodService.extractSignature(e, Marker.INTERFACE);
        var sourceMethodSignatureSet = Stream.of(annotationDetails.getSourceTypeElement())
                .map(sourceElement -> elementMethodService.extractSignature(sourceElement, Marker.SOURCE))
                .map(sourceMethods ->
                        ElementMethodService.filterMethodSignaturesByNames(sourceMethods, interfaceMethodSignatures))
                .findFirst().get();

        var details = MappingWrapperDetails.builder()
                .mappingWrapperNameDetails(mappingWrapperService.extractDetails(e))
                .annotationParamFullNames(mappingWrapperService.extractAnnotationParamFullNames(annotationDetails))
                .interfaceMethodSignatures(interfaceMethodSignatures)
                .sourceMethodSignatures(sourceMethodSignatureSet)
                .mapperMethodSignatures(
                        elementMethodService.extractSignature(annotationDetails.getMapperTypeElement(), Marker.MAPPER)
                ).build();

        final String classText = mappingWrapperImplClassCodeService.generateClassCode(details);

        createMappingWrapperImplementationClass(
                details.getMappingWrapperNameDetails().getImplementationFullName(), classText
        );
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
