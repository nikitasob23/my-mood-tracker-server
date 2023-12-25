package com.niksob.mapping_wrapper.service.element;

import com.niksob.mapping_wrapper.model.method_details.MethodSignature;
import com.niksob.mapping_wrapper.model.class_details.Marker;

import javax.lang.model.element.Element;
import java.util.Set;

public interface ElementMethodService {
    Set<MethodSignature> extractSignature(Element element, Marker marker);

    boolean filterMethodSignaturesByNames(MethodSignature source, Set<MethodSignature> filterParam);
}
