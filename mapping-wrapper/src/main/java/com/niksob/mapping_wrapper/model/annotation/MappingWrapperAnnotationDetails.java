package com.niksob.mapping_wrapper.model.annotation;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.lang.model.element.TypeElement;

@Data
@AllArgsConstructor
public class MappingWrapperAnnotationDetails {
    private final TypeElement sourceTypeElement;
    private final TypeElement mapperTypeElement;
}
