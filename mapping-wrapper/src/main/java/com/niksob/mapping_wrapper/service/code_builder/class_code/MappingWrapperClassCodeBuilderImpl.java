package com.niksob.mapping_wrapper.service.code_builder.class_code;

import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperDetails;
import com.niksob.mapping_wrapper.service.code_builder.method_code.MappingWrapperMethodCodeBuilder;
import com.niksob.mapping_wrapper.service.code_builder.method_code.MappingWrapperMethodCodeBuilderImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class MappingWrapperClassCodeBuilderImpl implements MappingWrapperClassCodeBuilder {
    private final StringBuilder classCode = new StringBuilder();

    private final MappingWrapperMethodCodeBuilder methodCodeBuilder;

    public MappingWrapperClassCodeBuilderImpl(MappingWrapperMethodCodeBuilderImpl methodCodeBuilder) {
        this.methodCodeBuilder = methodCodeBuilder;
    }

    @Override
    public MappingWrapperClassCodeBuilder addClassName(MappingWrapperDetails details) {
        classCode.append("""
                package %s;

                public class %s implements %s {
                """
                .formatted(
                        details.getMappingWrapperNameDetails().getPackageInterfaceName(),
                        details.getMappingWrapperNameDetails().getImplementationName(),
                        details.getMappingWrapperNameDetails().getFullInterfaceName()
                )
        );
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addFields(MappingWrapperDetails details) {
        classCode.append("""
                    private final %s source;
                    private final %s mapper;
                    
                """
                .formatted(
                        details.getAnnotationParamFullNames().getSourceClassFullName(),
                        details.getAnnotationParamFullNames().getMapperClassFullName()
                )
        );
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addConstructor(MappingWrapperDetails details) {
        classCode.append("""
                            public %s(
                                    %s source,
                                    %s mapper
                            ) {
                                this.source = source;
                                this.mapper = mapper;
                            }
                            
                        """.formatted(
                        details.getMappingWrapperNameDetails().getImplementationName(),
                        details.getAnnotationParamFullNames().getSourceClassFullName(),
                        details.getAnnotationParamFullNames().getMapperClassFullName()
                )
        );
        return this;
    }

    @Override
    public MappingWrapperClassCodeBuilder addMethods(MappingWrapperDetails details) {
        Stream.of(details)
                .map(methodCodeBuilder::build)
                .forEach(classCode::append);
        return this;
    }

    @Override
    public String build() {
        final String classCode = this.classCode.append("""
                }
                """).toString();
        clear();
        return classCode;
    }

    @Override
    public void clear() {
        methodCodeBuilder.clear();
        classCode.delete(0, classCode.length());
    }
}
