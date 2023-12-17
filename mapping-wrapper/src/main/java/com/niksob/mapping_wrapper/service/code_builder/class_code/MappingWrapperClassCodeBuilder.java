package com.niksob.mapping_wrapper.service.code_builder.class_code;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperDetails;

public interface MappingWrapperClassCodeBuilder {
    MappingWrapperClassCodeBuilder addClassName(MappingWrapperDetails details);

    MappingWrapperClassCodeBuilder addFields(MappingWrapperDetails details);

    MappingWrapperClassCodeBuilder addConstructor(MappingWrapperDetails details);

    MappingWrapperClassCodeBuilder addMethods(MappingWrapperDetails details);

    String build();

    void clear();
}
