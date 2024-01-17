package com.niksob.layer_connector.model.method_details;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MethodSignature {
    private final String methodName;
    private final String returnType;
    private final String paramType;
}
