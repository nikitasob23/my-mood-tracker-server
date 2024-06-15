package com.niksob.layer_connector.service.generation.code.layer_connector.clazz;

import com.niksob.layer_connector.model.class_details.LayerConnectorClassDetails;

public interface LayerConnectorCodeGenerator {
    String generateClassCode(LayerConnectorClassDetails details);
}
