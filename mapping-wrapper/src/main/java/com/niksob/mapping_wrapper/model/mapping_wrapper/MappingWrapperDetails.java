package com.niksob.mapping_wrapper.model.mapping_wrapper;

import com.niksob.mapping_wrapper.model.executable_element.MethodSignature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class MappingWrapperDetails {
    private final MappingWrapperNameDetails mappingWrapperNameDetails;
    private final MappingWrapperAnnotationParamFullNames annotationParamFullNames;
    private final Set<MethodSignature> interfaceMethodSignatures;
    private final Set<MethodSignature> sourceMethodSignatures;
    private final Set<MethodSignature> mapperMethodSignatures;
}
