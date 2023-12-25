package com.niksob.mapping_wrapper.model.class_details;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MappingWrapperClassDetails {
    private final ClassDetails interfaceDetails;
    private final ClassDetails sourceDetails;
    private final ClassDetails mapperDetails;
}
