package com.niksob.mapping_wrapper.service.code_generation.class_code;

import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;

public interface GenerateMappingWrapperCodeService {
    String generateClassCode(MappingWrapperClassDetails details);
}
