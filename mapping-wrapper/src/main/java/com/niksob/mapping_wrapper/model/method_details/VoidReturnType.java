package com.niksob.mapping_wrapper.model.method_details;

import lombok.Getter;

@Getter
public enum VoidReturnType {
    VOID("void"),
    VOID_OBJECT("Void"),
    GENERIC_PARAM("<Void>");

    private final String value;

    VoidReturnType(String value) {
        this.value = value;
    }
}
