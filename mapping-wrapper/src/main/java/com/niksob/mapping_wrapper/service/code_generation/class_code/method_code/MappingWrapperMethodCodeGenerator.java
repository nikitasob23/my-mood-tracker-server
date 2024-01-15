package com.niksob.mapping_wrapper.service.code_generation.class_code.method_code;

import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;

import java.util.List;

public interface MappingWrapperMethodCodeGenerator {
    List<String> generate(MappingWrapperClassDetails details);

    void clear();
}
