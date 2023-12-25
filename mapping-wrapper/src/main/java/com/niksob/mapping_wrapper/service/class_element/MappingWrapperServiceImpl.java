package com.niksob.mapping_wrapper.service.class_element;

import com.niksob.mapping_wrapper.model.mapping_wrapper.ClassDetails;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.model.mapping_wrapper.marker.Marker;
import com.niksob.mapping_wrapper.service.annotation.mapping_wrapper.MappingWrapperAnnotationService;
import com.niksob.mapping_wrapper.service.element.ElementMethodService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MappingWrapperServiceImpl implements MappingWrapperService {

    private final MappingWrapperAnnotationService annotationService;
    private final ElementMethodService elementMethodService;

    @Override
    public MappingWrapperClassDetails extractClassDetails(Element e) {
        var annotationDetails = annotationService.extractAnnotationDetails(e);

        var interfaceClassDetails = new ClassDetails(
                extractFullClassName((TypeElement) e),
                elementMethodService.extractSignature(e, Marker.INTERFACE)
        );
        var sourceClassDetails = new ClassDetails(
                extractFullClassName(annotationDetails.getSourceTypeElement()),
                elementMethodService.extractSignature(annotationDetails.getSourceTypeElement(), Marker.SOURCE)
                        .stream()
                        .filter(sourceMethod -> elementMethodService.filterMethodSignaturesByNames(
                                sourceMethod, interfaceClassDetails.getMethods())
                        ).collect(Collectors.toSet()));

        var mapperClassDetails = new ClassDetails(
                extractFullClassName(annotationDetails.getMapperTypeElement()),
                elementMethodService.extractSignature(annotationDetails.getMapperTypeElement(), Marker.MAPPER)
        );
        return new MappingWrapperClassDetails(interfaceClassDetails, sourceClassDetails, mapperClassDetails);
    }

    private String extractFullClassName(TypeElement e) {
        return e.getQualifiedName().toString();
    }
}
