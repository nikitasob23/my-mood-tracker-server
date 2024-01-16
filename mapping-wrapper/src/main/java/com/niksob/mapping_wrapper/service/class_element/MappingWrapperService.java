package com.niksob.mapping_wrapper.service.class_element;

import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.model.compiler.CompilationDetails;

import javax.lang.model.element.Element;
import java.util.Map;

public interface MappingWrapperService {
    String MAPPING_WRAPPER_NAME_POSTFIX = "MappingWrapper";

    MappingWrapperClassDetails extractClassDetails(Element e);

    CompilationDetails extractCompilationDetails(Map<String, String> options, String processorName);
}
