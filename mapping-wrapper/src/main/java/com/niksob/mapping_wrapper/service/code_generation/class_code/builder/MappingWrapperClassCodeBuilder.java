package com.niksob.mapping_wrapper.service.code_generation.class_code.builder;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperClassDetails;

public interface MappingWrapperClassCodeBuilder {
    MappingWrapperClassCodeBuilder builder(MappingWrapperClassDetails details);

    MappingWrapperClassCodeBuilder addClassName();

    MappingWrapperClassCodeBuilder addFields();

    MappingWrapperClassCodeBuilder addConstructor();

    MappingWrapperClassCodeBuilder addMethods();

    String build();

    void clear();
}
