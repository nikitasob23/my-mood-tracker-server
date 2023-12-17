package com.niksob.mapping_wrapper.service.element;

import com.niksob.mapping_wrapper.model.executable_element.MethodSignature;
import com.niksob.mapping_wrapper.model.mapping_wrapper.marker.Marker;

import javax.lang.model.element.Element;
import java.util.Set;
import java.util.stream.Collectors;

public interface ElementMethodService {
    Set<MethodSignature> extractSignature(Element element, Marker marker);

    static Set<MethodSignature> filterMethodSignaturesByNames(
            Set<MethodSignature> source, Set<MethodSignature> filterParam
    ) {
        final Set<String> namesToFilter = filterParam.stream()
                .map(MethodSignature::getMethodName)
                .collect(Collectors.toSet());
        return source.stream()
                .filter(methodDetails -> namesToFilter.contains(methodDetails.getMethodName()))
                .collect(Collectors.toSet());
    }
}
