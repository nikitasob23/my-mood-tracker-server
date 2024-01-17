package com.niksob.layer_connector.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LayerConnectorClassCode {
    private String packageName = "";
    private String generatedAnnotation = "";
    private String componentAnnotation = "";
    private String className = "";
    private List<String> fields = new ArrayList<>();
    private String constructor = "";
    private List<String> methods = new ArrayList<>();
    private String endChar = "";
}
