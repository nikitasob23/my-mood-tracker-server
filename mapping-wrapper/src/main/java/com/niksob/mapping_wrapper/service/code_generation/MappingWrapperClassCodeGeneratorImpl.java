package com.niksob.mapping_wrapper.service.code_generation;

import com.niksob.mapping_wrapper.service.code_builder.class_code.MappingWrapperClassCodeBuilder;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MappingWrapperClassCodeGeneratorImpl implements MappingWrapperClassCodeGenerator {
    private final MappingWrapperClassCodeBuilder mappingWrapperClassCodeBuilder;

    @Override
    public String generateClassCode(MappingWrapperDetails details) {
        try {
            return mappingWrapperClassCodeBuilder
                    .addClassName(details)
                    .addFields(details)
                    .addConstructor(details)
                    .addMethods(details)
                    .build();
        } catch (Exception e) {
            mappingWrapperClassCodeBuilder.clear();
            throw e;
        }
    }
}
