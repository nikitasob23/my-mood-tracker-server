package com.niksob.mapping_wrapper.model.class_details;

import com.niksob.mapping_wrapper.model.method_details.MethodSignature;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ClassDetails {
    private String name;
    private Set<MethodSignature> methods;
}
