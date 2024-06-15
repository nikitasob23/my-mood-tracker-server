package com.niksob.layer_connector.service.generation.code.layer_connector.method.builder;

import com.niksob.layer_connector.model.method_details.MappingWrapperMethodDetails;

public interface LayerConnectorMethodCodeBuilder {
    LayerConnectorMethodCodeBuilder builder(MappingWrapperMethodDetails details);

    LayerConnectorMethodCodeBuilder addMethodSignatureCode();

    LayerConnectorMethodCodeBuilder addMappingSourceParamCode();

    LayerConnectorMethodCodeBuilder addSourceMethodInvokingCode();

    LayerConnectorMethodCodeBuilder addMappingSourceReturnTypeCode();

    LayerConnectorMethodCodeBuilder addReturningResultCode();

    String build();

    void clear();
}
