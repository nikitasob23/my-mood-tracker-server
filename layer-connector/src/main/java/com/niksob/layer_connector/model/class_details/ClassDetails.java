package com.niksob.layer_connector.model.class_details;

import com.niksob.layer_connector.model.method_details.MethodSignature;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ClassDetails {
    private final String name;
    private final Set<MethodSignature> methods;
}
