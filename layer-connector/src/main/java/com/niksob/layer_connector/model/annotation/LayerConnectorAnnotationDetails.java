package com.niksob.layer_connector.model.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.lang.model.element.TypeElement;
import java.util.Set;

@Data
@AllArgsConstructor
public class LayerConnectorAnnotationDetails {
    private final TypeElement sourceTypeElement;
    private final Set<TypeElement> sourceParents;
    private final Set<TypeElement> mapperTypeElementSet;
    private final boolean springComponentEnabled;
}
