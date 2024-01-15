package com.niksob.mapping_wrapper.model.class_details;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MappingWrapperClassDetails {
    private final ClassDetails interfaceDetails;
    private final ClassDetails sourceDetails;
    private final List<ClassDetails> mapperDetailsList;
    private final boolean springComponentEnabled;
}
