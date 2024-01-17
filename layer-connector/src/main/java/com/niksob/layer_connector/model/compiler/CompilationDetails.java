package com.niksob.layer_connector.model.compiler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompilationDetails {
    private final String annotationProcessorName;
    private final String date;
    private final String projectVersion;
    private final String compilerName;
    private final String javaVersion;
}
