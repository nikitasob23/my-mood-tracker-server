package com.niksob.mapping_wrapper.service.code_generation.class_code.builder;

import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;

public interface MappingWrapperClassCodeBuilder {
    MappingWrapperClassCodeBuilder builder(MappingWrapperClassDetails details);

    MappingWrapperClassCodeBuilder addPackageName();

    MappingWrapperClassCodeBuilder addComponentAnnotation();

    MappingWrapperClassCodeBuilder addClassName();

    MappingWrapperClassCodeBuilder addFields();

    MappingWrapperClassCodeBuilder addConstructor();

    MappingWrapperClassCodeBuilder addMethods();

    String build();

    void clear();
}
