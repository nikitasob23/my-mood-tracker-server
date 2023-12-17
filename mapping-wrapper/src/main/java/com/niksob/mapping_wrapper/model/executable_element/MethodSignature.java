package com.niksob.mapping_wrapper.model.executable_element;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MethodSignature {
    private final String methodName;
    private final String returnType;
    private final String paramType;
}
