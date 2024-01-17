package com.niksob.layer_connector.service.annotation.layer_connector;

import com.niksob.layer_connector.model.annotation.MappingWrapperAnnotationDetails;

import javax.lang.model.element.Element;

public interface LayerConnectorAnnotationService {
    MappingWrapperAnnotationDetails extractAnnotationDetails(Element element);
}
