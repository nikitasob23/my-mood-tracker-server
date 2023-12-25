package com.niksob.mapping_wrapper.service.code_generation.class_code.builder;

import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.MappingWrapperMethodCodeGenerator;
import com.niksob.mapping_wrapper.util.ClassUtil;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component("MappingWrapperClassCodeBuilder")
public class MappingWrapperClassCodeBuilderImpl implements MappingWrapperClassCodeBuilder {
    private final StringBuilder classCode = new StringBuilder();
    private final MappingWrapperMethodCodeGenerator mappingWrapperMethodCodeGenerator;
    private final ClassUtil classUtil;
    private MappingWrapperClassDetails details;
    private boolean wasInitialized;

    public MappingWrapperClassCodeBuilderImpl(
            MappingWrapperMethodCodeGenerator mappingWrapperMethodCodeGenerator,
            ClassUtil classUtil
    ) {
        this.mappingWrapperMethodCodeGenerator = mappingWrapperMethodCodeGenerator;
        this.classUtil = classUtil;
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
    public MappingWrapperClassCodeBuilder addClassName() {
        final String interfaceFullName = details.getInterfaceDetails().getName();
        classCode.append("""
                package %s;

                public class %sMappingWrapper implements %s {
                """
                .formatted(
                        classUtil.getPackageName(interfaceFullName),
                        classUtil.getShortClassName(interfaceFullName),
                        interfaceFullName
                )
        );
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addFields() {
        classCode.append("""
                    private final %s source;
                    private final %s mapper;
                    
                """.formatted(
                details.getSourceDetails().getName(),
                details.getMapperDetails().getName())
        );
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addConstructor() {
        classCode.append("""
                    public %sMappingWrapper(
                            %s source,
                            %s mapper
                    ) {
                        this.source = source;
                        this.mapper = mapper;
                    }
                    
                """.formatted(
                classUtil.getShortClassName(details.getInterfaceDetails().getName()),
                details.getSourceDetails().getName(),
                details.getMapperDetails().getName())
        );
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addMethods() {
        Stream.of(details)
                .map(mappingWrapperMethodCodeGenerator::generate)
                .forEach(classCode::append);
        return this;
    }

    @Override
    public String build() {
        final String classCode = this.classCode.append("}").toString();
        clear();
        return classCode;
    }

    @Override
    public void clear() {
        mappingWrapperMethodCodeGenerator.clear();
        classCode.setLength(0);
        wasInitialized = false;
        details = null;
    }
}
