package com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.builder;

import com.niksob.mapping_wrapper.model.method_details.MethodSignature;
import com.niksob.mapping_wrapper.model.method_details.MappingWrapperMethodDetails;
import com.niksob.mapping_wrapper.model.method_details.VariableName;
import com.niksob.mapping_wrapper.util.ClassUtil;
import org.springframework.stereotype.Component;

@Component
public class MapperWrapperMethodCodeBuilderImpl implements MapperWrapperMethodCodeBuilder {
    private static final String ASSIGNMENT_OPERATOR = " = ";
    private final StringBuilder methodsCode = new StringBuilder();
    private final ClassUtil classUtil;

    private MethodSignature interfaceMethod;
    private MethodSignature methodForMappingSourceParam;
    private MethodSignature sourceMethod;
    private MethodSignature methodForMappingSourceReturnType;

    private boolean wasInitialized;

    public MapperWrapperMethodCodeBuilderImpl(ClassUtil classUtil) {
        this.classUtil = classUtil;
    }

    @Override
    public MapperWrapperMethodCodeBuilder builder(MappingWrapperMethodDetails details) {
        if (wasInitialized) {
            throw new IllegalStateException("The MappingWrapper builder %s has already been initialized"
                    .formatted(MapperWrapperMethodCodeBuilder.class.getSimpleName()));
        }
        wasInitialized = true;

        this.interfaceMethod = details.getInterfaceSignature();
        this.methodForMappingSourceParam = details.getMethodForMappingSourceParam();
        this.sourceMethod = details.getSourceMethod();
        this.methodForMappingSourceReturnType = details.getMethodForMappingSourceReturnType();
        return this;
    }

    @Override
    public MapperWrapperMethodCodeBuilder addMethodSignatureCode() {
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
    public MapperWrapperMethodCodeBuilder addMappingSourceParamCode() {
        // If the checks are not passed, the mapping of the current value will not be performed
        if (!classUtil.haveParam(interfaceMethod)) {
            return this;
        }
        if (!methodForMappingFound(methodForMappingSourceParam)) {
            return this;
        }
        methodsCode.append("""
                        %s %s = mapper.%s(%s);
                """.formatted(
                methodForMappingSourceParam.getReturnType(),
                VariableName.MAPPED.getValue(),
                methodForMappingSourceParam.getMethodName(),
                VariableName.VALUE.getValue()));
        return this;
    }

    @Override
    public MapperWrapperMethodCodeBuilder addSourceMethodInvokingCode() {
        var returnVoid = classUtil.returnVoid(sourceMethod);
        methodsCode.append("""
                        %s%s%ssource.%s(%s);
                """.formatted(
                returnVoid ? "" : sourceMethod.getReturnType(),
                returnVoid ? "" : " " + VariableName.RESULT.getValue(),
                returnVoid ? "" : ASSIGNMENT_OPERATOR,
                sourceMethod.getMethodName(),
                getSourceReturnTypeVarName()));
        return this;
    }

    @Override
    public MapperWrapperMethodCodeBuilder addMappingSourceReturnTypeCode() {
        // If the checks are not passed, the mapping of the current value will not be performed
        if (classUtil.returnVoid(sourceMethod)) {
            return this;
        }
        if (!methodForMappingFound(methodForMappingSourceReturnType)) {
            return this;
        }
        methodsCode.append("""
                        %s %s = mapper.%s(%s);
                """.formatted(
                methodForMappingSourceReturnType.getReturnType(),
                VariableName.MAPPED_RESULT.getValue(),
                methodForMappingSourceReturnType.getMethodName(),
                VariableName.RESULT.getValue())
        );
        return this;
    }

    @Override
    public MapperWrapperMethodCodeBuilder addReturningResultCode() {
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

    private boolean methodForMappingFound(MethodSignature mapperMethod) {
        return mapperMethod != null;
    }

    private String returnAndClearCode() {
        var methodsCodeString = methodsCode.toString();
        clear();
        return methodsCodeString;
    }
}
