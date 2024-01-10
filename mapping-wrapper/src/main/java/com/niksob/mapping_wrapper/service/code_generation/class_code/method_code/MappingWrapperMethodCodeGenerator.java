package com.niksob.mapping_wrapper.service.code_generation.class_code.method_code;

import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;

import java.util.Set;

public interface MappingWrapperMethodCodeGenerator {
    Set<String> generate(MappingWrapperClassDetails details);

    void clear();
}
