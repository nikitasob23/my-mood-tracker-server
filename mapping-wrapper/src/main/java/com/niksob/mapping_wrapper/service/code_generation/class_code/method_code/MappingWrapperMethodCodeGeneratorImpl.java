package com.niksob.mapping_wrapper.service.code_generation.class_code.method_code;

import com.niksob.mapping_wrapper.model.method_details.MethodSignature;
import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.model.method_details.MappingWrapperMethodDetails;
import com.niksob.mapping_wrapper.model.method_details.VoidReturnType;
import com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.builder.MapperWrapperMethodCodeBuilder;
import com.niksob.mapping_wrapper.util.ClassUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class MappingWrapperMethodCodeGeneratorImpl implements MappingWrapperMethodCodeGenerator {
    private final MapperWrapperMethodCodeBuilder codeBuilder;

    private final ClassUtil classUtil;

    @Override
    public String generate(MappingWrapperClassDetails classDetails) {
        var mapperClass = classDetails.getMapperDetails();
        if (methodHasDuplicates(mapperClass.getMethods())) {
            throw new IllegalStateException("The mapper %s has duplicates among the conversion methods"
                    .formatted(mapperClass.getName()));
        }
        try {
            return classDetails.getInterfaceDetails()
                    .getMethods().stream()
                    .map(interfaceMethod -> createMappingWrapperMethod(interfaceMethod, classDetails))
                    .filter(methodDetails -> checkMapperCompleteness(methodDetails, mapperClass.getName()))
                    .map(this::buildMethod)
                    .collect(Collectors.joining());
        } catch (Exception e) {
            codeBuilder.clear();
            throw e;
        }
    }

    @Override
    public void clear() {
        codeBuilder.clear();
    }

    private MappingWrapperMethodDetails createMappingWrapperMethod(
            MethodSignature interfaceMethod, MappingWrapperClassDetails details
    ) {
        var sourceMethod = details.getSourceDetails()
                .getMethods().stream()
                .filter(s -> s.getMethodName().equals(interfaceMethod.getMethodName()))
                .findFirst().orElseThrow(() -> notFoundSourceMethod(interfaceMethod, details));

        var mapperMethodForSourceParam = Stream.of(interfaceMethod.getParamType())
                .filter(Objects::nonNull)
                .flatMap(ignore -> details.getMapperDetails().getMethods().stream())
                .filter(m -> m.getParamType().equals(interfaceMethod.getParamType()))
                .filter(m -> m.getReturnType().equals(sourceMethod.getParamType()))
                .findFirst().orElse(null);

        var mapperMethodForSourceReturnType = Stream.of(interfaceMethod.getReturnType())
                .filter(returnType -> !returnType.equals(VoidReturnType.VOID.getValue()))
                .flatMap(ignore -> details.getMapperDetails().getMethods().stream())
                .filter(m -> m.getParamType().equals(sourceMethod.getReturnType()))
                .filter(m -> m.getReturnType().equals(interfaceMethod.getReturnType()))
                .findFirst().orElse(null);

        return new MappingWrapperMethodDetails(
                interfaceMethod, mapperMethodForSourceParam, sourceMethod, mapperMethodForSourceReturnType
        );
    }

    private boolean methodHasDuplicates(Set<MethodSignature> methods) {
        Set<MethodSignature> distinctMethods = new HashSet<>();
        for (MethodSignature method : methods) {
            Predicate<MethodSignature> compareParamAndReturnType = uniqueMethod ->
                    classUtil.compareParams(uniqueMethod, method) && classUtil.compareReturnTypes(uniqueMethod, method);

            if (distinctMethods.stream().noneMatch(compareParamAndReturnType)) {
                distinctMethods.add(method);
            } else {
                // A duplicate was found
                return true;
            }
        }
        // A duplicate was found
        return false;
    }

    private boolean checkMapperCompleteness(MappingWrapperMethodDetails methodDetails, String mapperName) {
        // Get method signatures
        final MethodSignature interfaceMethod = methodDetails.getInterfaceSignature();
        final MethodSignature sourceMethod = methodDetails.getSourceMethod();

        // Param checks
        boolean interfaceHasParam = classUtil.haveParam(interfaceMethod);
        boolean interfaceParamMappingRequired =
                interfaceHasParam && !classUtil.compareParams(interfaceMethod, sourceMethod);
        boolean paramMapperNotFound = methodDetails.getMethodForMappingSourceParam() == null;
        boolean paramMappingIsNotPossible = interfaceParamMappingRequired && paramMapperNotFound;

        // Return type checks
        boolean interfaceNotReturnsVoid = !classUtil.returnVoid(interfaceMethod);
        boolean interfaceReturnTypeMappingRequired =
                interfaceNotReturnsVoid && !classUtil.compareReturnTypes(interfaceMethod, sourceMethod);
        boolean returnTypeMapperNotFound = methodDetails.getMethodForMappingSourceReturnType() == null;
        boolean returnTypeMappingIsNotPossible = interfaceReturnTypeMappingRequired && returnTypeMapperNotFound;

        if (paramMappingIsNotPossible || returnTypeMappingIsNotPossible) {
            throw new IllegalStateException(
                    String.format("Mapper %s does not have a method for mapping all values", mapperName));
        }
        return true;
    }

    private String buildMethod(MappingWrapperMethodDetails MethodDetails) {
        return codeBuilder.builder(MethodDetails)
                .addMethodSignatureCode()
                .addMappingSourceParamCode()
                .addSourceMethodInvokingCode()
                .addMappingSourceReturnTypeCode()
                .addReturningResultCode()
                .build();
    }

    private IllegalStateException notFoundSourceMethod(
            MethodSignature interfaceMethod, MappingWrapperClassDetails details
    ) {
        return new IllegalStateException(
                "Didn't find source method to mapping parameter: %s of %s class's method %s".formatted(
                        interfaceMethod.getParamType(),
                        details.getSourceDetails().getName(),
                        interfaceMethod.getMethodName()));
    }
}
