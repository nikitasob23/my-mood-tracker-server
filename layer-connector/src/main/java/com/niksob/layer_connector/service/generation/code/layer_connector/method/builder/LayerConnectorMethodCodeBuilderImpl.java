package com.niksob.layer_connector.service.generation.code.layer_connector.method.builder;

import com.niksob.layer_connector.model.method_details.MappingMethodDetails;
import com.niksob.layer_connector.model.method_details.MethodSignature;
import com.niksob.layer_connector.model.method_details.MappingWrapperMethodDetails;
import com.niksob.layer_connector.model.method_details.VariableName;
import com.niksob.layer_connector.util.clazz.ClassUtil;
import org.springframework.stereotype.Component;

@Component
public class LayerConnectorMethodCodeBuilderImpl implements LayerConnectorMethodCodeBuilder {
    private final StringBuilder methodsCode = new StringBuilder();
    private final ClassUtil classUtil;

    private MethodSignature interfaceMethod;
    private MappingMethodDetails methodForMappingSourceParam;
    private MethodSignature sourceMethod;
    private MappingMethodDetails methodForMappingSourceReturnType;

    private boolean wasInitialized;

    public LayerConnectorMethodCodeBuilderImpl(ClassUtil classUtil) {
        this.classUtil = classUtil;
    }

    @Override
    public LayerConnectorMethodCodeBuilder builder(MappingWrapperMethodDetails details) {
        if (wasInitialized) {
            throw new IllegalStateException("The LayerConnector builder %s has already been initialized"
                    .formatted(LayerConnectorMethodCodeBuilder.class.getSimpleName()));
        }
        wasInitialized = true;

        this.interfaceMethod = details.getInterfaceSignature();
        this.methodForMappingSourceParam = details.getMethodForMappingSourceParam();
        this.sourceMethod = details.getSourceMethod();
        this.methodForMappingSourceReturnType = details.getMethodForMappingSourceReturnType();
        return this;
    }

    @Override
    public LayerConnectorMethodCodeBuilder addMethodSignatureCode() {
        var haveParam = classUtil.haveParam(interfaceMethod);
        methodsCode.append("""
                    @Override
                    public %s %s(%s%s) {
                """.formatted(
                interfaceMethod.getReturnType(),
                interfaceMethod.getMethodName(),
                haveParam ? interfaceMethod.getParamType() + " " : "",
                haveParam ? VariableName.VALUE.getValue() : ""));
        return this;
    }

    @Override
    public LayerConnectorMethodCodeBuilder addMappingSourceParamCode() {
        // If the checks are not passed, the mapping of the current value will not be performed
        if (!classUtil.haveParam(interfaceMethod)) {
            return this;
        }
        if (!methodForMappingFound(methodForMappingSourceParam)) {
            return this;
        }
        methodsCode.append("""
                        %s %s = %s.%s(%s);
                """.formatted(
                methodForMappingSourceParam.getReturnType(),
                VariableName.MAPPED.getValue(),
                methodForMappingSourceParam.getVariableName(),
                methodForMappingSourceParam.getMethodName(),
                VariableName.VALUE.getValue()));
        return this;
    }

    @Override
    public LayerConnectorMethodCodeBuilder addSourceMethodInvokingCode() {
        var returnVoid = classUtil.returnVoid(sourceMethod);
        methodsCode.append("        ")
                .append(returnVoid ? "" : sourceMethod.getReturnType())
                .append(returnVoid ? "" : " " + VariableName.RESULT.getValue())
                .append(returnVoid ? "" : " = ")
                .append(VariableName.SOURCE.getValue())
                .append(".")
                .append(sourceMethod.getMethodName())
                .append(String.format("(%s);\n", getSourceReturnTypeVarName()));
        return this;
    }

    @Override
    public LayerConnectorMethodCodeBuilder addMappingSourceReturnTypeCode() {
        // If the checks are not passed, the mapping of the current value will not be performed
        if (classUtil.returnVoid(sourceMethod)) {
            return this;
        }
        if (!methodForMappingFound(methodForMappingSourceReturnType)) {
            return this;
        }
        methodsCode.append("""
                        %s %s = %s.%s(%s);
                """.formatted(
                methodForMappingSourceReturnType.getReturnType(),
                VariableName.MAPPED_RESULT.getValue(),
                methodForMappingSourceReturnType.getVariableName(),
                methodForMappingSourceReturnType.getMethodName(),
                VariableName.RESULT.getValue())
        );
        return this;
    }

    @Override
    public LayerConnectorMethodCodeBuilder addReturningResultCode() {
        if (classUtil.returnVoid(interfaceMethod)) {
            return this;
        }
        methodsCode.append("""
                        return %s;
                """.formatted(getReturnTypeVarName()));
        return this;
    }

    @Override
    public String build() {
        methodsCode.append("    }");
        return returnAndClearCode();
    }

    @Override
    public void clear() {
        methodsCode.setLength(0);

        interfaceMethod = null;
        methodForMappingSourceParam = null;
        sourceMethod = null;
        methodForMappingSourceReturnType = null;

        wasInitialized = false;
    }

    private String getSourceReturnTypeVarName() {
        if (!classUtil.haveParam(sourceMethod)) {
            return "";
        }
        if (methodForMappingFound(methodForMappingSourceParam)) {
            return VariableName.MAPPED.getValue();
        }
        return VariableName.VALUE.getValue();
    }

    private String getReturnTypeVarName() {
        if (methodForMappingFound(methodForMappingSourceReturnType)) {
            return VariableName.MAPPED_RESULT.getValue();
        }
        return VariableName.RESULT.getValue();
    }

    private boolean methodForMappingFound(MappingMethodDetails mapperMethod) {
        return mapperMethod != null;
    }

    private String returnAndClearCode() {
        var methodsCodeString = methodsCode.toString();
        clear();
        return methodsCodeString;
    }
}
