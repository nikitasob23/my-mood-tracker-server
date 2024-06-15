package com.niksob.layer_connector.service.generation.code.layer_connector.clazz.builder;

import com.niksob.layer_connector.model.class_details.LayerConnectorClassDetails;

public interface LayerConnectorClassCodeBuilder {
    LayerConnectorClassCodeBuilder builder(LayerConnectorClassDetails details);

    LayerConnectorClassCodeBuilder addPackageName();

    LayerConnectorClassCodeBuilder addGeneratedAnnotation();

    LayerConnectorClassCodeBuilder addComponentAnnotation();

    LayerConnectorClassCodeBuilder addClassName();

    LayerConnectorClassCodeBuilder addFields();

    LayerConnectorClassCodeBuilder addConstructor();

    LayerConnectorClassCodeBuilder addMethods();

    String build();

    void clear();
}
