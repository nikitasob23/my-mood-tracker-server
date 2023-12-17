package com.niksob.mapping_wrapper.service.annotation.mapping_wrapper;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperAnnotationDetails;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperAnnotationParamFullNames;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperNameDetails;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.function.Supplier;

public interface MappingWrapperAnnotationService {
    MappingWrapperAnnotationDetails extractAnnotationDetails(Element element);

    MappingWrapperAnnotationParamFullNames extractAnnotationParamFullNames(
            MappingWrapperAnnotationDetails annotationDetails
    );

    MappingWrapperNameDetails extractDetails(Element element);

    TypeElement extractTypeElement(Supplier<Class<?>> annotationParamSupplier);
}
