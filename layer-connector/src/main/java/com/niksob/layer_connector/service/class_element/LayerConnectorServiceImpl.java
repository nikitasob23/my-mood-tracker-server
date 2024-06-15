package com.niksob.layer_connector.service.class_element;

import com.niksob.layer_connector.logger.Logger;
import com.niksob.layer_connector.model.annotation.LayerConnectorAnnotationDetails;
import com.niksob.layer_connector.model.class_details.ClassDetails;
import com.niksob.layer_connector.model.class_details.LayerConnectorClassDetails;
import com.niksob.layer_connector.model.class_details.Marker;
import com.niksob.layer_connector.model.compiler.CompilationDetails;
import com.niksob.layer_connector.service.annotation.layer_connector.LayerConnectorAnnotationService;
import com.niksob.layer_connector.service.element.ElementMethodService;
import com.niksob.layer_connector.util.date.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LayerConnectorServiceImpl implements LayerConnectorService {

    private final LayerConnectorAnnotationService annotationService;
    private final ElementMethodService elementMethodService;
    private final DateUtil dateUtil;

    private final Logger log;

    @Override
    public LayerConnectorClassDetails extractClassDetails(Element e) {
        var annotationDetails = annotationService.extractAnnotationDetails(e);

        var interfaceClassDetails = new ClassDetails(
                extractFullClassName((TypeElement) e),
                elementMethodService.extractSignature(e, Marker.INTERFACE)
        );
        final var sourceClassDetails = extractSourceClassDetails(annotationDetails, interfaceClassDetails);

        var mapperClassDetailsList = annotationDetails.getMapperTypeElementSet().stream()
                .map(typeElement -> new ClassDetails(
                        extractFullClassName(typeElement),
                        elementMethodService.extractSignature(typeElement, Marker.MAPPER)
                )).toList();
        return new LayerConnectorClassDetails(
                interfaceClassDetails, sourceClassDetails, mapperClassDetailsList,
                annotationDetails.isSpringComponentEnabled()
        );
    }

    @Override
    public CompilationDetails extractCompilationDetails(Map<String, String> options, String processorName) {
        var compilationDetails = new CompilationDetails(
                processorName,
                dateUtil.generateCurrentDate(),
                options.get("project.version"),
                options.get("compiler.name"),
                options.get("java.version")
        );
        log.warn("Compilation details extracted: " + compilationDetails);
        return compilationDetails;
    }

    private ClassDetails extractSourceClassDetails(
            LayerConnectorAnnotationDetails annotationDetails, ClassDetails interfaceClassDetails
    ) {
        final String sourceClassName = extractFullClassName(annotationDetails.getSourceTypeElement());

        var sourceParentsMethods = annotationDetails.getSourceParents().stream()
                .map(sourceParent -> elementMethodService.extractSignature(sourceParent, Marker.SOURCE))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        var sourceMethods = elementMethodService.extractSignature(
                annotationDetails.getSourceTypeElement(), Marker.SOURCE
        );
        sourceMethods.addAll(sourceParentsMethods);

        var filteredSourceMethodsByInterface = sourceMethods.stream()
                .filter(sourceMethod -> elementMethodService.filterMethodSignaturesByNames(
                        sourceMethod, interfaceClassDetails.getMethods())
                ).collect(Collectors.toSet());
        return new ClassDetails(sourceClassName, filteredSourceMethodsByInterface);
    }

    private String extractFullClassName(TypeElement e) {
        return e.getQualifiedName().toString();
    }
}
