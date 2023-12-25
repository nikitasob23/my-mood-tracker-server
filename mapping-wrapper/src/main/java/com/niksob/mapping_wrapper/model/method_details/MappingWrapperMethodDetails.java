package com.niksob.mapping_wrapper.model.method_details;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MappingWrapperMethodDetails {
    MethodSignature interfaceSignature;
    MethodSignature methodForMappingSourceParam;
    MethodSignature sourceMethod;
    MethodSignature methodForMappingSourceReturnType;
}
