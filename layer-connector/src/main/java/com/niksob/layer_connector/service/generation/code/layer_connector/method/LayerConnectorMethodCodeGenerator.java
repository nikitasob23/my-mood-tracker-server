package com.niksob.layer_connector.service.generation.code.layer_connector.method;

import com.niksob.layer_connector.model.class_details.LayerConnectorClassDetails;

import java.util.List;

public interface LayerConnectorMethodCodeGenerator {
    List<String> generate(LayerConnectorClassDetails details);

    void clear();
}
