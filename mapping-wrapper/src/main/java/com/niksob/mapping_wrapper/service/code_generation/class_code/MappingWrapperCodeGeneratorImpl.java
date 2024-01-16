package com.niksob.mapping_wrapper.service.code_generation.class_code;

import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.service.code_generation.class_code.builder.MappingWrapperClassCodeBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MappingWrapperCodeGeneratorImpl implements MappingWrapperCodeGenerator {
    private final MappingWrapperClassCodeBuilder mappingWrapperClassCodeBuilder;

    @Override
    public String generateClassCode(MappingWrapperClassDetails details) {
        try {
            return mappingWrapperClassCodeBuilder.builder(details)
                    .addPackageName()
                    .addGeneratedAnnotation()
                    .addComponentAnnotation()
                    .addClassName()
                    .addFields()
                    .addConstructor()
                    .addMethods()
                    .build();
        } catch (Exception e) {
            mappingWrapperClassCodeBuilder.clear();
            throw e;
        }
    }
}
