package com.niksob.layer_connector.model.method_details;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class MappingMethodDetails {
    private final String className;
    private final String variableName;
    private final String methodName;
    private final String returnType;
    private final String paramType;
}
