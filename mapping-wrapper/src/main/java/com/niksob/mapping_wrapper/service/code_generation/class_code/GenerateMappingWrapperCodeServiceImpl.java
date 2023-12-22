package com.niksob.mapping_wrapper.service.code_generation.class_code;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.service.code_generation.class_code.builder.MappingWrapperClassCodeBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GenerateMappingWrapperCodeServiceImpl implements GenerateMappingWrapperCodeService {
    private final MappingWrapperClassCodeBuilder mappingWrapperClassCodeBuilder;

    @Override
    public String generateClassCode(MappingWrapperClassDetails details) {
        try {
            return mappingWrapperClassCodeBuilder.builder(details)
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
