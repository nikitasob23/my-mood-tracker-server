package com.niksob.layer_connector.service.annotation.layer_connector;

import com.niksob.layer_connector.annotation.LayerConnector;
import com.niksob.layer_connector.logger.Logger;
import com.niksob.layer_connector.model.annotation.MappingWrapperAnnotationDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LayerConnectorAnnotationServiceImpl implements LayerConnectorAnnotationService {
    private final Logger log;

    @Override
    public MappingWrapperAnnotationDetails extractAnnotationDetails(Element element) {
        var annotation = element.getAnnotation(LayerConnector.class);

        var sourceTypeElement = extractTypeElement(annotation::source);
        var mapperTypeElement = extractTypeElements(annotation::mapper);
        var springComponentEnabled = annotation.isSpringComponentEnabled();
        var annotationDetails =
                new MappingWrapperAnnotationDetails(sourceTypeElement, mapperTypeElement, springComponentEnabled);

        if (sourceTypeElement == null || mapperTypeElement == null || mapperTypeElement.isEmpty()) {
            throw new IllegalArgumentException("Invalid @LayerConnector annotation parameters");
        }

        log.warn("MappingWrapperAnnotationDetails was extracted. ObjectState = " + annotationDetails);
        return annotationDetails;
    }

    private TypeElement extractTypeElement(Supplier<Class<?>> annotationParamSupplier) {
        try {
            // Will raise MirroredTypeException
            annotationParamSupplier.get();
        } catch (MirroredTypeException mte) {
            DeclaredType sourceTypeMirror = (DeclaredType) mte.getTypeMirror();
            return (TypeElement) sourceTypeMirror.asElement();
        }
        throw new IllegalStateException("Unable to extract the parameter as a Type Element from the @LayerConnector annotation");
    }

    private Set<TypeElement> extractTypeElements(Supplier<Class<?>[]> annotationParamSupplier) {
        try {
            // Will raise MirroredTypesException
            annotationParamSupplier.get();
        } catch (MirroredTypesException mte) {
            return mte.getTypeMirrors().stream()
                    .map(typeMirror -> (DeclaredType) typeMirror)
                    .map(DeclaredType::asElement)
                    .map(e -> (TypeElement) e)
                    .collect(Collectors.toSet());
        }
        throw new IllegalStateException("It was not possible to extract the parameter as a Type Element from the @LayerConnector annotation");
    }
}