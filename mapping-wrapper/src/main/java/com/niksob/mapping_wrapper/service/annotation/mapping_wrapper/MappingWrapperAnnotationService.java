package com.niksob.mapping_wrapper.service.annotation.mapping_wrapper;

import com.niksob.mapping_wrapper.model.annotation.MappingWrapperAnnotationDetails;

import javax.lang.model.element.Element;

public interface MappingWrapperAnnotationService {
    MappingWrapperAnnotationDetails extractAnnotationDetails(Element element);
}
