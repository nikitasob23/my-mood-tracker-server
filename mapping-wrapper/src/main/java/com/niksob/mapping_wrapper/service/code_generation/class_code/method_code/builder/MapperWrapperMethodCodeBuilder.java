package com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.builder;

import com.niksob.mapping_wrapper.model.method_details.MappingWrapperMethodDetails;

public interface MapperWrapperMethodCodeBuilder {
    MapperWrapperMethodCodeBuilder builder(MappingWrapperMethodDetails details);

    MapperWrapperMethodCodeBuilder addMethodSignatureCode();

    MapperWrapperMethodCodeBuilder addMappingSourceParamCode();

    MapperWrapperMethodCodeBuilder addSourceMethodInvokingCode();

    MapperWrapperMethodCodeBuilder addMappingSourceReturnTypeCode();

    MapperWrapperMethodCodeBuilder addReturningResultCode();

    String build();

    void clear();
}
