package com.niksob.layer_connector.processor;

import com.google.auto.service.AutoService;
import com.niksob.layer_connector.di.component.MappingWrapperProcessorDIComponent;
import com.niksob.layer_connector.di.module.logger.LoggerDIModule;
import com.niksob.layer_connector.model.compiler.CompilationDetails;
import com.niksob.layer_connector.service.class_element.LayerConnectorService;
import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.LayerConnectorCodeGenerator;
import com.niksob.layer_connector.util.clazz.ClassUtil;
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
@SupportedAnnotationTypes("com.niksob.layer_connector.annotation.LayerConnector")
@Setter
@Accessors(chain = true)
public class LayerConnectorProcessor extends AbstractProcessor {
    private LayerConnectorCodeGenerator layerConnectorCodeGenerator;
    private LayerConnectorService layerConnectorService;
    private ClassUtil classUtil;

    private boolean processorEnable;

    private CompilationDetails compilationDetails;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        MappingWrapperProcessorDIComponent.builder()
                .loggerDIModule(new LoggerDIModule(processingEnv))
                .build()
                .inject(this);

        this.compilationDetails = layerConnectorService.extractCompilationDetails(
                processingEnv.getOptions(),
                LayerConnectorProcessor.class.getCanonicalName()
        );
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!processorEnable) {
            return false;
        }
        for (var element : annotations) {
            roundEnv.getElementsAnnotatedWith(element)
                    .forEach(this::generateLayerConnectorClass);
        }
        return true;
    }

    private void generateLayerConnectorClass(Element e) {
        var layerConnectorClassDetails = layerConnectorService.extractClassDetails(e)
                .setCompilationDetails(compilationDetails);
        var classCode = layerConnectorCodeGenerator.generateClassCode(layerConnectorClassDetails);
        var layerConnectorName = classUtil.stickNames(
                layerConnectorClassDetails.getInterfaceDetails().getName(),
                LayerConnectorService.MAPPING_WRAPPER_NAME_POSTFIX
        );
        createLayerConnectorImplementationClass(layerConnectorName, classCode);
    }

    private void createLayerConnectorImplementationClass(String implementationFullName, String s) {
        try (PrintWriter writer = new PrintWriter(
                processingEnv.getFiler().createSourceFile(implementationFullName).openWriter())
        ) {
            writer.println(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
