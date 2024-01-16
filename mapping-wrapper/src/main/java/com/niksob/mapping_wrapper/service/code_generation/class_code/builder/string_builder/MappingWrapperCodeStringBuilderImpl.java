package com.niksob.mapping_wrapper.service.code_generation.class_code.builder.string_builder;

import com.niksob.mapping_wrapper.model.MappingWrapperClassCode;
import org.springframework.stereotype.Component;

@Component
public class MappingWrapperCodeStringBuilderImpl implements MappingWrapperCodeStringBuilder {
    private static final String NEW_LINE = "\n";
    private static final String EMPTY_LINE = "\n\n";

    @Override
    public String build(MappingWrapperClassCode classCode) {
        final StringBuilder codeStringBuilder = new StringBuilder(classCode.getPackageName())
                .append(EMPTY_LINE)
                .append(classCode.getGeneratedAnnotation())
                .append(classCode.getComponentAnnotation())
                .append(NEW_LINE)
                .append(classCode.getClassName())
                .append(NEW_LINE);
        classCode.getFields().forEach(field -> codeStringBuilder.append(field).append(NEW_LINE));
        codeStringBuilder.append(NEW_LINE)
                .append(classCode.getConstructor())
                .append(NEW_LINE);
        classCode.getMethods().forEach(method -> codeStringBuilder.append(method).append(EMPTY_LINE));
        codeStringBuilder.append(classCode.getEndChar());
        return codeStringBuilder.toString();
    }
}
