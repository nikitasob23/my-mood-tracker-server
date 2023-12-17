package com.niksob.mapping_wrapper.model.mapping_wrapper;

import com.niksob.mapping_wrapper.model.executable_element.MethodSignature;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MappingDetails {
    MethodSignature interfaceMethod;
    MethodSignature sourceMethod;
    MethodSignature mapperMethodForSourceParam;
    MethodSignature mapperMethodForSourceReturnType;
}
