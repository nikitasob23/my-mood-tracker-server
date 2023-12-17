package com.niksob.mapping_wrapper.service.code_generation;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperDetails;

public interface MappingWrapperClassCodeGenerator {
    String generateClassCode(MappingWrapperDetails details);
}
