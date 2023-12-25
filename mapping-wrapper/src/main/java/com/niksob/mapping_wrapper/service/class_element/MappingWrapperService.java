package com.niksob.mapping_wrapper.service.class_element;

import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;

import javax.lang.model.element.Element;

public interface MappingWrapperService {
    String MAPPING_WRAPPER_NAME_POSTFIX = "MappingWrapper";

    MappingWrapperClassDetails extractClassDetails(Element e);
}
