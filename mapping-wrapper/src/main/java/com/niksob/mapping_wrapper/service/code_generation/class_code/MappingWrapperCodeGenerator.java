package com.niksob.mapping_wrapper.service.code_generation.class_code;

import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;

public interface MappingWrapperCodeGenerator {
    String generateClassCode(MappingWrapperClassDetails details);
}
