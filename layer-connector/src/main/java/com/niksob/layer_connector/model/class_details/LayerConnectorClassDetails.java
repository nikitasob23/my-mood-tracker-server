package com.niksob.layer_connector.model.class_details;

import com.niksob.layer_connector.model.compiler.CompilationDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class LayerConnectorClassDetails {
    private final ClassDetails interfaceDetails;
    private final ClassDetails sourceDetails;
    private final List<ClassDetails> mapperDetailsList;
    private final boolean springComponentEnabled;
    @Accessors(chain = true)
    private CompilationDetails compilationDetails;
}
