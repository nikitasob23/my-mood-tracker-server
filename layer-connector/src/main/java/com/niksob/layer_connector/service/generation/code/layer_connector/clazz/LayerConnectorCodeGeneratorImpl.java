package com.niksob.layer_connector.service.generation.code.layer_connector.clazz;

import com.niksob.layer_connector.model.class_details.LayerConnectorClassDetails;
import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.builder.LayerConnectorClassCodeBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LayerConnectorCodeGeneratorImpl implements LayerConnectorCodeGenerator {
    private final LayerConnectorClassCodeBuilder layerConnectorClassCodeBuilder;

    @Override
    public String generateClassCode(LayerConnectorClassDetails details) {
        try {
            return layerConnectorClassCodeBuilder.builder(details)
                    .addPackageName()
                    .addGeneratedAnnotation()
                    .addComponentAnnotation()
                    .addClassName()
                    .addFields()
                    .addConstructor()
                    .addMethods()
                    .build();
        } catch (Exception e) {
            layerConnectorClassCodeBuilder.clear();
            throw e;
        }
    }
}
