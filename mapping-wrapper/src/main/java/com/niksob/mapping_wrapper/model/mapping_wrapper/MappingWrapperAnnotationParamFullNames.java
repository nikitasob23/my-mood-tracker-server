package com.niksob.mapping_wrapper.model.mapping_wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MappingWrapperAnnotationParamFullNames {
    private final String sourceClassFullName;
    private final String mapperClassFullName;
}
