package com.niksob.mapping_wrapper.model.mapping_wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.lang.model.element.TypeElement;

@Data
@AllArgsConstructor
public class MappingWrapperAnnotationDetails {
    private final TypeElement sourceTypeElement;
    private final TypeElement mapperTypeElement;
}
