package com.niksob.mapping_wrapper.model.mapping_wrapper;

import lombok.Getter;

@Getter
public enum VariableName {
    VALUE("value"),
    MAPPED("mapped"),
    RESULT("result"),
    MAPPED_RESULT("mappedResult");

    private final String value;

    VariableName(String value) {
        this.value = value;
    }
}
