package com.niksob.mapping_wrapper.processor;

import com.google.auto.service.AutoService;
import com.niksob.mapping_wrapper.ProcessorLogger;
import com.niksob.mapping_wrapper.service.code_builder.class_code.MappingWrapperClassCodeBuilderImpl;
import com.niksob.mapping_wrapper.service.code_builder.method_code.MappingWrapperMethodCodeBuilderImpl;
import com.niksob.mapping_wrapper.model.mapping_wrapper.marker.Marker;
import com.niksob.mapping_wrapper.model.mapping_wrapper.*;
import com.niksob.mapping_wrapper.service.annotation.mapping_wrapper.MappingWrapperAnnotationService;
import com.niksob.mapping_wrapper.service.code_generation.MappingWrapperClassCodeGenerator;
import com.niksob.mapping_wrapper.service.element.ElementMethodService;
import com.niksob.mapping_wrapper.service.element.ElementMethodServiceImpl;
import com.niksob.mapping_wrapper.service.code_generation.MappingWrapperClassCodeGeneratorImpl;
import com.niksob.mapping_wrapper.service.annotation.mapping_wrapper.MappingWrapperAnnotationServiceImpl;
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
    private MappingWrapperClassCodeGenerator mappingWrapperClassCodeGenerator;
    private MappingWrapperAnnotationService mappingWrapperAnnotationService;
    private ElementMethodService elementMethodService;

    private boolean processorEnable;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        var methodCodeBuilder = new MappingWrapperMethodCodeBuilderImpl();
        var mappingWrapperClassCodeBuilder = new MappingWrapperClassCodeBuilderImpl(methodCodeBuilder);
        var logger = new ProcessorLogger(processingEnv);

        this.mappingWrapperClassCodeGenerator = new MappingWrapperClassCodeGeneratorImpl(mappingWrapperClassCodeBuilder);
        this.mappingWrapperAnnotationService = new MappingWrapperAnnotationServiceImpl(logger);
        this.elementMethodService = new ElementMethodServiceImpl(logger);
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
        var annotationDetails = mappingWrapperAnnotationService.extractAnnotationDetails(e);
        var interfaceMethodSignatures =
                elementMethodService.extractSignature(e, Marker.INTERFACE);
        var sourceMethodSignatureSet = Stream.of(annotationDetails.getSourceTypeElement())
                .map(sourceElement -> elementMethodService.extractSignature(sourceElement, Marker.SOURCE))
                .map(sourceMethods ->
                        ElementMethodService.filterMethodSignaturesByNames(sourceMethods, interfaceMethodSignatures))
                .findFirst().get();

        var details = MappingWrapperDetails.builder()
                .mappingWrapperNameDetails(mappingWrapperAnnotationService.extractDetails(e))
                .annotationParamFullNames(
                        mappingWrapperAnnotationService.extractAnnotationParamFullNames(annotationDetails)
                ).interfaceMethodSignatures(interfaceMethodSignatures)
                .sourceMethodSignatures(sourceMethodSignatureSet)
                .mapperMethodSignatures(
                        elementMethodService.extractSignature(annotationDetails.getMapperTypeElement(), Marker.MAPPER)
                ).build();

        var classText = mappingWrapperClassCodeGenerator.generateClassCode(details);

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

