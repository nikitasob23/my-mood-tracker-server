package com.niksob.layer_connector.service.class_element;

import com.niksob.layer_connector.model.class_details.LayerConnectorClassDetails;
import com.niksob.layer_connector.model.compiler.CompilationDetails;

import javax.lang.model.element.Element;
import java.util.Map;

public interface LayerConnectorService {
    String MAPPING_WRAPPER_NAME_POSTFIX = "LayerConnector";

    LayerConnectorClassDetails extractClassDetails(Element e);

    CompilationDetails extractCompilationDetails(Map<String, String> options, String processorName);
}
