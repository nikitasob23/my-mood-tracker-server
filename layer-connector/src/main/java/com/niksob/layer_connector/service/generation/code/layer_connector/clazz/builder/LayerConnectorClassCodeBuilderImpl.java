package com.niksob.layer_connector.service.generation.code.layer_connector.clazz.builder;

import com.niksob.layer_connector.model.LayerConnectorClassCode;
import com.niksob.layer_connector.model.class_details.ClassDetails;
import com.niksob.layer_connector.model.class_details.LayerConnectorClassDetails;
import com.niksob.layer_connector.model.method_details.VariableName;
import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.builder.string.LayerConnectorCodeStringBuilder;
import com.niksob.layer_connector.service.generation.code.layer_connector.method.LayerConnectorMethodCodeGenerator;
import com.niksob.layer_connector.util.clazz.ClassUtil;
import com.niksob.layer_connector.values.LayerConnectorValues;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LayerConnectorClassCodeBuilderImpl implements LayerConnectorClassCodeBuilder {
    private LayerConnectorClassCode classCode = new LayerConnectorClassCode();
    private final LayerConnectorMethodCodeGenerator layerConnectorMethodCodeGenerator;
    private final ClassUtil classUtil;
    private final LayerConnectorCodeStringBuilder layerConnectorCodeStringBuilder;

    private final List<String> fieldNames = new ArrayList<>();
    private final List<String> declaringVariables = new ArrayList<>();
    private LayerConnectorClassDetails details;
    private boolean wasInitialized;

    public LayerConnectorClassCodeBuilderImpl(
            LayerConnectorMethodCodeGenerator layerConnectorMethodCodeGenerator,
            ClassUtil classUtil,
            LayerConnectorCodeStringBuilder layerConnectorCodeStringBuilder
    ) {
        this.layerConnectorMethodCodeGenerator = layerConnectorMethodCodeGenerator;
        this.classUtil = classUtil;
        this.layerConnectorCodeStringBuilder = layerConnectorCodeStringBuilder;
    }

    @Override
    public LayerConnectorClassCodeBuilder builder(LayerConnectorClassDetails details) {
        if (wasInitialized) {
            throw new IllegalStateException("The LayerConnector builder %s has already been initialized"
                    .formatted(LayerConnectorClassCodeBuilder.class.getSimpleName()));
        }
        wasInitialized = true;
        this.details = details;
        initFieldNames(details);
        initDeclaringVariables(details);
        return this;
    }

    @Override
    public LayerConnectorClassCodeBuilder addPackageName() {
        final String interfaceFullName = details.getInterfaceDetails().getName();
        classCode.setPackageName(String.format("package %s;", classUtil.getPackageName(interfaceFullName)));
        return this;
    }

    @Override
    public LayerConnectorClassCodeBuilder addGeneratedAnnotation() {
        var compilationDetails = details.getCompilationDetails();
        var comments = String.format("comments = version: %s, compiler: %s, environment: Java %s",
                compilationDetails.getProjectVersion(),
                compilationDetails.getCompilerName(),
                compilationDetails.getJavaVersion()
        );
        classCode.setGeneratedAnnotation(String.format("""
                        @javax.annotation.processing.Generated(
                            value = "%s",
                            date = "%s",
                            comments = "%s"
                        )
                        """,
                compilationDetails.getAnnotationProcessorName(),
                compilationDetails.getDate(),
                comments
        ));
        return this;
    }

    @Override
    public LayerConnectorClassCodeBuilder addComponentAnnotation() {
        if (details.isSpringComponentEnabled()) {
            classCode.setComponentAnnotation("@org.springframework.stereotype.Component");
        }
        return this;
    }

    @Override
    public LayerConnectorClassCodeBuilder addClassName() {
        final String interfaceFullName = details.getInterfaceDetails().getName();
        classCode.setClassName(String.format(
                "public class %s%s implements %s {",
                classUtil.getShortClassName(interfaceFullName),
                LayerConnectorValues.POSTFIX.getValue(),
                interfaceFullName
        ));
        return this;
    }

    @Override
    public LayerConnectorClassCodeBuilder addFields() {
        final List<String> fields = declaringVariables.stream()
                .map(declaringVariable -> String.format("    private final %s;", declaringVariable))
                .toList();
        classCode.setFields(fields);
        return this;
    }

    @Override
    public LayerConnectorClassCodeBuilder addConstructor() {
        final String params = declaringVariables.stream()
                .map(declaringVariable -> "        " + declaringVariable)
                .reduce((v1, v2) -> v1 + ",\n" + v2)
                .orElseThrow(this::throwIfFieldsAbsent);

        final String fieldsInitialization = fieldNames.stream()
                .map(fieldName -> String.format("        this.%s = %s;", fieldName, fieldName))
                .reduce((v1, v2) -> v1 + "\n" + v2)
                .orElseThrow(this::throwIfFieldsAbsent);

        classCode.setConstructor(String.format("""
                            public %s%s(
                        %s
                            ) {
                        %s
                            }
                        """,
                classUtil.getShortClassName(details.getInterfaceDetails().getName()),
                LayerConnectorValues.POSTFIX.getValue(),
                params,
                fieldsInitialization
        ));
        return this;
    }

    private IllegalStateException throwIfFieldsAbsent() {
        return new IllegalStateException("There are no mappers or source class in LayerConnector");
    }

    @Override
    public LayerConnectorClassCodeBuilder addMethods() {
        var methods = layerConnectorMethodCodeGenerator.generate(details);
        classCode.setMethods(methods);
        return this;
    }

    @Override
    public String build() {
        classCode.setEndChar("}");
        var codeStr = layerConnectorCodeStringBuilder.build(classCode);
        clear();
        return codeStr;
    }

    @Override
    public void clear() {
        layerConnectorMethodCodeGenerator.clear();
        classCode = new LayerConnectorClassCode();
        fieldNames.clear();
        declaringVariables.clear();
        wasInitialized = false;
        details = null;
    }

    private void initFieldNames(LayerConnectorClassDetails details) {
        details.getMapperDetailsList().stream()
                .map(ClassDetails::getName)
                .map(classUtil::getVariableName)
                .forEach(fieldNames::add);
        fieldNames.add(VariableName.SOURCE.getValue());
    }

    private void initDeclaringVariables(LayerConnectorClassDetails details) {
        final List<ClassDetails> mapperDetailsList = details.getMapperDetailsList();
        for (int i = 0; i < mapperDetailsList.size(); i++) {
            var className = mapperDetailsList.get(i).getName();
            var declaringVariable = String.format("%s %s", className, fieldNames.get(i));
            declaringVariables.add(declaringVariable);
        }
        var declaringSourceField = String.format("%s source", details.getSourceDetails().getName());
        declaringVariables.add(declaringSourceField);
    }
}
