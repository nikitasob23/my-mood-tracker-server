package com.niksob.mapping_wrapper.model.method_details;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MappingWrapperMethodDetails {
    private final MethodSignature interfaceSignature;
    private final MappingMethodDetails methodForMappingSourceParam;
    private final MethodSignature sourceMethod;
    private final MappingMethodDetails methodForMappingSourceReturnType;
}
