package com.niksob.mapping_wrapper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassCodeExample {
    private String classBeginning;
    private Set<String> methods;
    private String classEnding;
}
