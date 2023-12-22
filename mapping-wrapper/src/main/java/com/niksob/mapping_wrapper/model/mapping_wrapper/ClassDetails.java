package com.niksob.mapping_wrapper.model.mapping_wrapper;

import com.niksob.mapping_wrapper.model.executable_element.MethodSignature;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ClassDetails {
    private String name;
    private Set<MethodSignature> methods;
}
