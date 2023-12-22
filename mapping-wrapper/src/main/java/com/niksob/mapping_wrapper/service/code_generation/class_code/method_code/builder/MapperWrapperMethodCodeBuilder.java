package com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.builder;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperMethodDetails;

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
