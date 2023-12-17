package com.niksob.mapping_wrapper.service.code_builder.method_code;

import com.niksob.mapping_wrapper.model.executable_element.MethodSignature;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingDetails;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperDetails;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MappingWrapperMethodCodeBuilderImpl implements MappingWrapperMethodCodeBuilder {
    private static final String VOID_RETURN_TYPE = "void";
    private final StringBuilder methodsCode = new StringBuilder();

    @Override
    public String build(MappingWrapperDetails details) {
        try {
            var sameInterfaceAndSourceMethods = Stream.of(details)
                    .flatMap(this::getSameInterfaceAndSourceMethods)
                    .peek(this::addSameMethodsCode)
                    .collect(Collectors.toSet());

            excludeSameMethodsByName(details.getInterfaceMethodSignatures(), sameInterfaceAndSourceMethods)
                    .forEach(interfaceMethod -> addMethodCode(details, interfaceMethod));
        } catch (Exception e) {
            throw new CodeGenerationException(e);
        }
        return returnAndClearCode();
    }

    @Override
    public void clear() {
        methodsCode.delete(0, methodsCode.length());
    }

    private String returnAndClearCode() {
        var methodsCodeString = methodsCode.toString();
        clear();
        return methodsCodeString;
    }

    private void addMethodCode(MappingWrapperDetails details, MethodSignature interfaceMethod) {
        var sourceMethod = details.getSourceMethodSignatures().stream()
                .filter(s -> s.getMethodName().equals(interfaceMethod.getMethodName()))
                .findFirst().orElseThrow(() -> notFoundSourceMethod(interfaceMethod, details));

        var mapperMethodForSourceParam = Stream.of(interfaceMethod.getParamType())
                .filter(Objects::nonNull)
                .flatMap(ignore -> details.getMapperMethodSignatures().stream())
                .filter(m -> m.getParamType().equals(interfaceMethod.getParamType()))
                .filter(m -> m.getReturnType().equals(sourceMethod.getParamType()))
                .findFirst().orElse(null);

        var mapperMethodForSourceReturnType = Stream.of(interfaceMethod.getReturnType())
                .filter(returnType -> !returnType.equals(VOID_RETURN_TYPE))
                .flatMap(ignore -> details.getMapperMethodSignatures().stream())
                .filter(m -> m.getParamType().equals(sourceMethod.getReturnType()))
                .filter(m -> m.getReturnType().equals(interfaceMethod.getReturnType()))
                .findFirst().orElse(null);

        var mappingDetails = new MappingDetails(
                interfaceMethod, sourceMethod, mapperMethodForSourceParam, mapperMethodForSourceReturnType
        );

        if (!methodHaveParam(interfaceMethod)) {
            addOnlyReturnTypeMappingCode(mappingDetails);
            return;
        } else if (methodReturnVoid(interfaceMethod)) {
            addOnlyParamMappingCode(mappingDetails);
            return;
        }

        if (mapperMethodForSourceParam == null) {
            notFoundMethodToMappingParam(interfaceMethod, details);
        } else if (mapperMethodForSourceReturnType == null) {
            notFoundMethodToMappingReturnType(sourceMethod, details);
        }
        addParamAndReturnTypeMappingCode(mappingDetails);
    }

    private static boolean methodHaveParam(MethodSignature interfaceMethod) {
        return interfaceMethod.getParamType() != null;
    }

    private static boolean methodReturnVoid(MethodSignature interfaceMethod) {
        return interfaceMethod.getReturnType().equals("void");
    }

    private static Set<MethodSignature> excludeSameMethodsByName(
            Set<MethodSignature> source, Set<MethodSignature> excluded
    ) {
        final Set<String> excludedNames = excluded.stream()
                .map(MethodSignature::getMethodName)
                .collect(Collectors.toSet());
        return source.stream()
                .filter(interfaceMethodDetails -> !excludedNames.contains(interfaceMethodDetails.getMethodName()))
                .collect(Collectors.toSet());
    }

    private static boolean filterSameSignature(Set<MethodSignature> methodSignatures, MethodSignature interfaceMethod) {
        return methodSignatures.stream()
                .filter(s -> interfaceMethod.getMethodName().equals(s.getMethodName()))
                .filter(s -> interfaceMethod.getParamType() != null)
                .filter(s -> interfaceMethod.getParamType().equals(s.getParamType()))
                .anyMatch(s -> interfaceMethod.getReturnType().equals(s.getReturnType()));
    }

    private Stream<MethodSignature> getSameInterfaceAndSourceMethods(MappingWrapperDetails details) {
        return details.getInterfaceMethodSignatures().stream()
                .filter(interfaceMethod -> filterSameSignature(details.getSourceMethodSignatures(), interfaceMethod));
    }

    private IllegalStateException notFoundSourceMethod(MethodSignature interfaceMethod, MappingWrapperDetails details) {
        return new IllegalStateException(
                "Didn't find source method to mapping parameter: %s of %s class's method %s".formatted(
                        interfaceMethod.getParamType(),
                        details.getAnnotationParamFullNames().getSourceClassFullName(),
                        interfaceMethod.getMethodName()));
    }

    private void notFoundMethodToMappingParam(
            MethodSignature interfaceMethod, MappingWrapperDetails details
    ) {
        throw new IllegalStateException(
                "Didn't find mapper method to mapping parameter: %s of %s class's method %s".formatted(
                        interfaceMethod.getParamType(),
                        details.getAnnotationParamFullNames().getSourceClassFullName(),
                        interfaceMethod.getMethodName()
                ));
    }

    private void notFoundMethodToMappingReturnType(
            MethodSignature sourceMethod, MappingWrapperDetails details
    ) {
        throw new IllegalStateException(String.format(
                "Didn't find mapper method to mapping return type: %s of %s class's method %s".formatted(
                        sourceMethod.getReturnType(),
                        details.getAnnotationParamFullNames().getSourceClassFullName(),
                        sourceMethod.getMethodName()
                )));
    }

    private void addSameMethodsCode(MethodSignature methodSignature) {
        addMethodNameCode(methodSignature);
        methodsCode.append("""
                        return source.%s(value);
                    }
                    
                """.formatted(methodSignature.getMethodName())
        );
    }

    private void addMethodNameCode(MethodSignature methodSignature) {
        methodsCode.append("""
                    @Override
                    public %s %s(%s value) {
                """.formatted(
                methodSignature.getReturnType(),
                methodSignature.getMethodName(),
                methodSignature.getParamType()
        ));
    }

    private void addMappingInterfaceParam(MethodSignature mapperMethodForSourceParam) {
        methodsCode.append("""
                        final %s mapped = mapper.%s(value);
                """.formatted(mapperMethodForSourceParam.getReturnType(), mapperMethodForSourceParam.getMethodName())
        );
    }

    private void addParamAndReturnTypeMappingCode(MappingDetails mappingDetails) {
        addMethodNameCode(mappingDetails.getInterfaceMethod());
        addMappingInterfaceParam(mappingDetails.getMapperMethodForSourceParam());
        methodsCode.append("""
                                final %s result = source.%s(mapped);
                                return mapper.%s(result);
                            }
                            
                        """.formatted(
                        mappingDetails.getMapperMethodForSourceReturnType().getParamType(),
                        mappingDetails.getInterfaceMethod().getMethodName(),
                        mappingDetails.getMapperMethodForSourceReturnType().getMethodName()
                )
        );
    }

    private void addOnlyParamMappingCode(MappingDetails mappingDetails) {
        addMethodNameCode(mappingDetails.getInterfaceMethod());
        addMappingInterfaceParam(mappingDetails.getMapperMethodForSourceParam());
        methodsCode.append("""
                        source.%s(mapped);
                    }
                    
                """.formatted(mappingDetails.getSourceMethod().getMethodName()));
    }

    private void addOnlyReturnTypeMappingCode(MappingDetails mappingDetails) {
        methodsCode.append("""
                    @Override
                    public %s %s() {
                """.formatted(
                mappingDetails.getInterfaceMethod().getReturnType(),
                mappingDetails.getInterfaceMethod().getMethodName()
        ));
        methodsCode.append("""
                        final %s result = source.%s();
                        return mapper.%s(result);
                    }
                    
                """.formatted(
                mappingDetails.getSourceMethod().getReturnType(),
                mappingDetails.getSourceMethod().getMethodName(),
                mappingDetails.getMapperMethodForSourceReturnType().getMethodName()
        ));
    }
}
