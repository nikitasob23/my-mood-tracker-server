package com.niksob.layer_connector.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MethodSignatureValues {
    PARAM_TYPE("paramType"),
    RETURN_TYPE("returnType");

    private final String value;
}
