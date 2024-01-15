package com.niksob.mapping_wrapper.service.code_generation.class_code.builder;

import com.niksob.mapping_wrapper.model.MappingWrapperClassCode;
import com.niksob.mapping_wrapper.model.class_details.ClassDetails;
import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.model.method_details.VariableName;
import com.niksob.mapping_wrapper.service.code_generation.class_code.builder.string_builder.MappingWrapperCodeStringBuilder;
import com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.MappingWrapperMethodCodeGenerator;
import com.niksob.mapping_wrapper.util.ClassUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("MappingWrapperClassCodeBuilder")
public class MappingWrapperClassCodeBuilderImpl implements MappingWrapperClassCodeBuilder {
    private MappingWrapperClassCode classCode = new MappingWrapperClassCode();
    private final MappingWrapperMethodCodeGenerator mappingWrapperMethodCodeGenerator;
    private final ClassUtil classUtil;
    private final MappingWrapperCodeStringBuilder mappingWrapperCodeStringBuilder;

    private final List<String> fieldNames = new ArrayList<>();
    private final List<String> declaringVariables = new ArrayList<>();
    private MappingWrapperClassDetails details;
    private boolean wasInitialized;

    public MappingWrapperClassCodeBuilderImpl(
            MappingWrapperMethodCodeGenerator mappingWrapperMethodCodeGenerator,
            ClassUtil classUtil,
            MappingWrapperCodeStringBuilder mappingWrapperCodeStringBuilder
    ) {
        this.mappingWrapperMethodCodeGenerator = mappingWrapperMethodCodeGenerator;
        this.classUtil = classUtil;
        this.mappingWrapperCodeStringBuilder = mappingWrapperCodeStringBuilder;
    }

    @Override
    public MappingWrapperClassCodeBuilder builder(MappingWrapperClassDetails details) {
        if (wasInitialized) {
            throw new IllegalStateException("The MappingWrapper builder %s has already been initialized"
                    .formatted(MappingWrapperClassCodeBuilder.class.getSimpleName()));
        }
        wasInitialized = true;
        this.details = details;
        initFieldNames(details);
        initDeclaringVariables(details);
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addPackageName() {
        final String interfaceFullName = details.getInterfaceDetails().getName();
        classCode.setPackageName(String.format("package %s;", classUtil.getPackageName(interfaceFullName)));
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addComponentAnnotation() {
        if (details.isSpringComponentEnabled()) {
            classCode.setComponentAnnotation("@org.springframework.stereotype.Component");
        }
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addClassName() {
        final String interfaceFullName = details.getInterfaceDetails().getName();
        classCode.setClassName(String.format(
                "public class %sMappingWrapper implements %s {",
                classUtil.getShortClassName(interfaceFullName), interfaceFullName
        ));
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addFields() {
        final List<String> fields = declaringVariables.stream()
                .map(declaringVariable -> String.format("    private final %s;", declaringVariable))
                .toList();
        classCode.setFields(fields);
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addConstructor() {
        final String params = declaringVariables.stream()
                .map(declaringVariable -> "        " + declaringVariable)
                .reduce((v1, v2) -> v1 + ",\n" + v2)
                .orElseThrow(this::throwIfFieldsAbsent);

        final String fieldsInitialization = fieldNames.stream()
                .map(fieldName -> String.format("        this.%s = %s;", fieldName, fieldName))
                .reduce((v1, v2) -> v1 + "\n" + v2)
                .orElseThrow(this::throwIfFieldsAbsent);

        classCode.setConstructor(String.format("""
                            public %sMappingWrapper(
                        %s
                            ) {
                        %s
                            }
                        """,
                classUtil.getShortClassName(details.getInterfaceDetails().getName()),
                params,
                fieldsInitialization
        ));
        return this;
    }

    private IllegalStateException throwIfFieldsAbsent() {
        return new IllegalStateException("There are no mappers or source class in MappingWrapper");
    }

    @Override
    public MappingWrapperClassCodeBuilder addMethods() {
        var methods = mappingWrapperMethodCodeGenerator.generate(details);
        classCode.setMethods(methods);
        return this;
    }

    @Override
    public String build() {
        classCode.setEndChar("}");
        var codeStr = mappingWrapperCodeStringBuilder.build(classCode);
        clear();
        return codeStr;
    }

    @Override
    public void clear() {
        mappingWrapperMethodCodeGenerator.clear();
        classCode = new MappingWrapperClassCode();
        fieldNames.clear();
        declaringVariables.clear();
        wasInitialized = false;
        details = null;
    }

    private void initFieldNames(MappingWrapperClassDetails details) {
        details.getMapperDetailsList().stream()
                .map(ClassDetails::getName)
                .map(classUtil::getVariableName)
                .forEach(fieldNames::add);
        fieldNames.add(VariableName.SOURCE.getValue());
    }

    private void initDeclaringVariables(MappingWrapperClassDetails details) {
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
