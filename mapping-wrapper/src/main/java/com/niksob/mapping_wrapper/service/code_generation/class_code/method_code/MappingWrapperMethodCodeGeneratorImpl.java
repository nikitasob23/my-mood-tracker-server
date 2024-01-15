package com.niksob.mapping_wrapper.service.code_generation.class_code.method_code;

import com.niksob.mapping_wrapper.mapper.MappingMethodDetailsMapper;
import com.niksob.mapping_wrapper.model.method_details.MethodSignature;
import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.model.method_details.MappingWrapperMethodDetails;
import com.niksob.mapping_wrapper.model.method_details.VoidReturnType;
import com.niksob.mapping_wrapper.service.code_generation.class_code.method_code.builder.MapperWrapperMethodCodeBuilder;
import com.niksob.mapping_wrapper.util.ClassUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class MappingWrapperMethodCodeGeneratorImpl implements MappingWrapperMethodCodeGenerator {
    private final MapperWrapperMethodCodeBuilder codeBuilder;

    private final ClassUtil classUtil;
    private final MappingMethodDetailsMapper mappingMethodDetailsMapper;

    @Override
    public List<String> generate(MappingWrapperClassDetails classDetails) {

        classDetails.getMapperDetailsList().stream()
                .filter(mapperClass -> methodHasDuplicates(mapperClass.getMethods()))
                .findAny().ifPresent(mapperClass -> {
                    throw new IllegalStateException("The mapper %s has duplicates among the conversion methods"
                            .formatted(mapperClass.getName()));
                });
        try {
            return classDetails.getInterfaceDetails()
                    .getMethods().stream()
                    .map(interfaceMethod -> createMappingWrapperMethod(interfaceMethod, classDetails))
                    .filter(methodDetails ->
                            checkMapperCompleteness(methodDetails, classDetails.getInterfaceDetails().getName())
                    ).map(this::buildMethod)
                    .toList();
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
                .findFirst().orElseThrow(() -> notFoundSourceMethod(details));

        var mappingMethodDetails = details.getMapperDetailsList().stream()
                .map(mapperClass -> mappingMethodDetailsMapper.map(
                        mapperClass, classUtil.getVariableName(mapperClass.getName()))
                ).flatMap(Collection::stream)
                .collect(Collectors.toSet());

        var mapperMethodForSourceParam = Stream.of(interfaceMethod.getParamType())
                .filter(Objects::nonNull)
                .flatMap(ignore -> mappingMethodDetails.stream())
                .filter(m -> m.getParamType().equals(interfaceMethod.getParamType()))
                .filter(m -> m.getReturnType().equals(sourceMethod.getParamType()))
                .findFirst().orElse(null);

        var mapperMethodForSourceReturnType = Stream.of(interfaceMethod.getReturnType())
                .filter(returnType -> !returnType.equals(VoidReturnType.VOID.getValue()))
                .flatMap(ignore -> mappingMethodDetails.stream())
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

    private boolean checkMapperCompleteness(MappingWrapperMethodDetails methodDetails, String interfaceName) {
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
                    String.format(
                            "Mappers needed for MappingWrapper interface %s does not have a method for mapping all values",
                            interfaceName
                    ));
        }
        return true;
    }

    private String buildMethod(MappingWrapperMethodDetails methodDetails) {
        return codeBuilder.builder(methodDetails)
                .addMethodSignatureCode()
                .addMappingSourceParamCode()
                .addSourceMethodInvokingCode()
                .addMappingSourceReturnTypeCode()
                .addReturningResultCode()
                .build();
    }

    private IllegalStateException notFoundSourceMethod(MappingWrapperClassDetails details) {
        return new IllegalStateException("There are fewer methods declared in the source class %s than in the MappingWrapper interface %s. Remove unnecessary methods in the MappingWrapper or add-ons to the source class"
                .formatted(
                        details.getSourceDetails().getName(),
                        details.getInterfaceDetails().getName()
                ));
    }
}
