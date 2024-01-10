package com.niksob.mapping_wrapper.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MappingWrapperClassCode {
    private String packageName = "";
    private String componentAnnotation = "";
    private String className = "";
    private Set<String> fields = new HashSet<>();
    private String constructor = "";
    private Set<String> methods = new HashSet<>();
    private String endChar = "";
}
