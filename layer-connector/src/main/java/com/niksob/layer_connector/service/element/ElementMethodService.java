package com.niksob.layer_connector.service.element;

import com.niksob.layer_connector.model.method_details.MethodSignature;
import com.niksob.layer_connector.model.class_details.Marker;

import javax.lang.model.element.Element;
import java.util.Set;

public interface ElementMethodService {
    Set<MethodSignature> extractSignature(Element element, Marker marker);

    boolean filterMethodSignaturesByNames(MethodSignature source, Set<MethodSignature> filterParam);
}
