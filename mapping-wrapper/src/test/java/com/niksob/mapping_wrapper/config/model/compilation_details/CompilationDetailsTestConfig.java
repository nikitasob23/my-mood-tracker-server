package com.niksob.mapping_wrapper.config.model.compilation_details;

import com.niksob.mapping_wrapper.model.compiler.CompilationDetails;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CompilationDetailsTestConfig {
    @Bean
    public CompilationDetails getCompilationDetails() {
        return new CompilationDetails(
                "com.niksob.mapping_wrapper.processor.MappingWrapperProcessor",
                "2024-01-16T12:11:03+0300",
                "0.0.1-SNAPSHOT",
                "javac",
                "17"
        );
    }
}
