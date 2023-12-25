package com.niksob.mapping_wrapper.service.element;

import com.niksob.mapping_wrapper.model.executable_element.MethodSignature;
import com.niksob.mapping_wrapper.model.mapping_wrapper.marker.Marker;

import javax.lang.model.element.Element;
import java.util.Set;

public interface ElementMethodService {
    Set<MethodSignature> extractSignature(Element element, Marker marker);

    boolean filterMethodSignaturesByNames(MethodSignature source, Set<MethodSignature> filterParam);
}
