package com.niksob.mapping_wrapper.service.annotation.mapping_wrapper;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperAnnotationDetails;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperAnnotationParamFullNames;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperNameDetails;
import com.niksob.mapping_wrapper.processor.MappingWrapper;
import lombok.AllArgsConstructor;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import java.util.function.Supplier;

@AllArgsConstructor
public class MappingWrapperAnnotationServiceImpl implements MappingWrapperAnnotationService {
    @Override
    public MappingWrapperAnnotationDetails extractAnnotationDetails(Element element) {
        final MappingWrapper annotation = element.getAnnotation(MappingWrapper.class);

        final TypeElement sourceTypeElement = extractTypeElement(annotation::source);
        final TypeElement mapperTypeElement = extractTypeElement(annotation::mapper);
        return new MappingWrapperAnnotationDetails(sourceTypeElement, mapperTypeElement);
    }

    @Override
    public MappingWrapperAnnotationParamFullNames extractAnnotationParamFullNames(
            MappingWrapperAnnotationDetails annotationDetails
    ) {
        String sourceClassFullName = annotationDetails.getSourceTypeElement()
                .getQualifiedName()
                .toString();
        String mapperClassFullName = annotationDetails.getMapperTypeElement()
                .getQualifiedName()
                .toString();
        return new MappingWrapperAnnotationParamFullNames(sourceClassFullName, mapperClassFullName);
    }

    @Override
    public MappingWrapperNameDetails extractDetails(Element element) {
        final String interfaceName = element.getSimpleName().toString();
        final String packageInterfaceName = element.getEnclosingElement().toString();
        return new MappingWrapperNameDetails(interfaceName, packageInterfaceName);
    }

    @Override
    public TypeElement extractTypeElement(Supplier<Class<?>> annotationParamSupplier) {
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
