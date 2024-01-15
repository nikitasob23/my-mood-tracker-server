package com.niksob.mapping_wrapper.model.method_details;

import lombok.Getter;

@Getter
public enum VariableName {
    SOURCE("source"),
    VALUE("value"),
    MAPPED("mapped"),
    RESULT("result"),
    MAPPED_RESULT("mappedResult");

    private final String value;

    VariableName(String value) {
        this.value = value;
    }
}
