package com.niksob.mapping_wrapper.service.annotation.mapping_wrapper;

import com.niksob.mapping_wrapper.logger.Logger;
import com.niksob.mapping_wrapper.model.annotation.MappingWrapperAnnotationDetails;
import com.niksob.mapping_wrapper.annotation.MappingWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class MappingWrapperAnnotationServiceImpl implements MappingWrapperAnnotationService {
    private final Logger log;

    @Override
    public MappingWrapperAnnotationDetails extractAnnotationDetails(Element element) {
        var annotation = element.getAnnotation(MappingWrapper.class);

        var sourceTypeElement = extractTypeElement(annotation::source);
        var mapperTypeElement = extractTypeElement(annotation::mapper);
        var springComponentEnabled = annotation.isSpringComponentEnabled();
        var annotationDetails =
                new MappingWrapperAnnotationDetails(sourceTypeElement, mapperTypeElement, springComponentEnabled);

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
        throw new IllegalStateException("It was not possible to extract the parameter as a Type Element from the @MappingWrapper annotation");
    }
}