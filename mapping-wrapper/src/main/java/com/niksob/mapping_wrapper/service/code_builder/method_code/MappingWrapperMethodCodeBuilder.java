package com.niksob.mapping_wrapper.service.code_builder.method_code;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperDetails;

public interface MappingWrapperMethodCodeBuilder {
    String build(MappingWrapperDetails details);

    void clear();
}
