package com.niksob.mapping_wrapper.service.code_generation.class_code.method_code;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperClassDetails;

public interface MappingWrapperMethodCodeGenerator {
    String generate(MappingWrapperClassDetails details);

    void clear();
}
