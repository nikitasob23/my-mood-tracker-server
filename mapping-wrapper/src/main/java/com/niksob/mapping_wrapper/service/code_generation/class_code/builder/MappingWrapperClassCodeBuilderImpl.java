package com.niksob.mapping_wrapper.service.code_generation.class_code.builder;

import com.niksob.mapping_wrapper.model.MappingWrapperClassCode;
import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.service.code_generation.class_code.builder.string_builder.MappingWrapperCodeStringBuilder;
import com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.MappingWrapperMethodCodeGenerator;
import com.niksob.mapping_wrapper.util.ClassUtil;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("MappingWrapperClassCodeBuilder")
public class MappingWrapperClassCodeBuilderImpl implements MappingWrapperClassCodeBuilder {
    private MappingWrapperClassCode classCode = new MappingWrapperClassCode();
    private final MappingWrapperMethodCodeGenerator mappingWrapperMethodCodeGenerator;
    private final ClassUtil classUtil;
    private final MappingWrapperCodeStringBuilder mappingWrapperCodeStringBuilder;
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
        var fields = Set.of(
                String.format("    private final %s source;", details.getSourceDetails().getName()),
                String.format("    private final %s mapper;", details.getMapperDetails().getName())
        );
        classCode.setFields(fields);
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addConstructor() {
        classCode.setConstructor(String.format("""
                            public %sMappingWrapper(
                                %s source,
                                %s mapper
                            ) {
                                this.source = source;
                                this.mapper = mapper;
                            }
                        """,
                classUtil.getShortClassName(details.getInterfaceDetails().getName()),
                details.getSourceDetails().getName(),
                details.getMapperDetails().getName())
        );
        return this;
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
        wasInitialized = false;
        details = null;
    }
}
