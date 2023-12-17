package com.niksob.mapping_wrapper.service;

import com.niksob.domain.model.user.Nickname;
import com.niksob.mapping_wrapper.MainContextTest;
import com.niksob.mapping_wrapper.ProcessorLogger;
import com.niksob.mapping_wrapper.model.*;
import com.niksob.mapping_wrapper.model.executable_element.MethodSignature;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperAnnotationParamFullNames;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperDetails;
import com.niksob.mapping_wrapper.model.mapping_wrapper.MappingWrapperNameDetails;
import com.niksob.mapping_wrapper.service.code_generation.MappingWrapperClassCodeGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.CodeGenerationException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MappingWrapperClassCodeGeneratorTest extends MainContextTest {
    private static String MAPPING_WRAPPER_CLASS_EXAMPLE;

    @Autowired
    private MappingWrapperClassCodeGenerator mappingWrapperClassCodeGenerator;

    @MockBean
    private ProcessorLogger log;

    private MappingWrapperDetails mappingWrapperDetails;

    @BeforeAll
    public static void readExampleMappingWrapperClassCodeExamples() {
        String filePath = "./src/test/resources/mapping_wrapper_class.example";
        try {
            MAPPING_WRAPPER_CLASS_EXAMPLE = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read MappingWrapper class text example", e);
        }
    }

    @Test
    public void testClassCodeGeneration() {
        prepare(
                /*mappingWrapperInterface = */
                /*sourceClass = */UserEntityDaoImpl.class,
                /*mapperClass = */UserEntityMapper.class
        );
        final String classCode = mappingWrapperClassCodeGenerator.generateClassCode(mappingWrapperDetails);
        assertThat(classCode).isEqualTo(MAPPING_WRAPPER_CLASS_EXAMPLE);
    }

    @Test
    public void testGeneratedClassTextWithWrongSourceClass() {
        prepare(
                /*mappingWrapperInterface = */
                /*sourceClass = */Nickname.class,
                /*mapperClass = */UserEntityMapper.class
        );
        Assertions.assertThatThrownBy(() -> mappingWrapperClassCodeGenerator.generateClassCode(mappingWrapperDetails))
                .isInstanceOf(CodeGenerationException.class)
                .hasCauseInstanceOf(IllegalStateException.class)
                .hasMessage("java.lang.IllegalStateException-->Didn't find source method to mapping parameter: com.niksob.domain.model.user.Username of com.niksob.domain.model.user.Nickname class's method load");
    }

    @Test
    public void testWithMapperWithInsufficientMethodsForConverting() {
        prepare(
                /*mappingWrapperInterface = */
                /*sourceClass = */UserEntityDaoImpl.class,
                /*mapperClass = */TestWrongUserMapper.class
        );
        Assertions.assertThatThrownBy(() -> mappingWrapperClassCodeGenerator.generateClassCode(mappingWrapperDetails))
                .isInstanceOf(CodeGenerationException.class)
                .hasCauseInstanceOf(IllegalStateException.class)
                .hasMessage("java.lang.IllegalStateException-->Didn't find mapper method to mapping parameter: com.niksob.domain.model.user.Username of com.niksob.mapping_wrapper.model.UserEntityDaoImpl class's method load");
    }

    private void prepare(Class<?> sourceClass, Class<?> mapperClass) {
        final Class<?> interfaceClass = UserDao.class;
        final MappingWrapperNameDetails mappingWrapperNameDetails = new MappingWrapperNameDetails(
                interfaceClass.getSimpleName(), interfaceClass.getPackageName()
        );

        final MappingWrapperAnnotationParamFullNames annotationParamFullNames =
                new MappingWrapperAnnotationParamFullNames(
                        sourceClass.getCanonicalName(),
                        mapperClass.getCanonicalName()
                );

        final Set<MethodSignature> interfaceMethodSignatureSet = getMethods(interfaceClass);

        final Set<MethodSignature> sourceMethodSignatureSet = getMethods(sourceClass).stream()
                .filter(methodDetails -> interfaceMethodSignatureSet.stream()
                        .map(MethodSignature::getMethodName)
                        .anyMatch(methodDetails.getMethodName()::equals)
                )
                .collect(Collectors.toSet());

        final Set<MethodSignature> mapperMethodSignatureSet = Stream.of(mapperClass)
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .map(this::extractMethodDetails)
                .collect(Collectors.toSet());

        mappingWrapperDetails = MappingWrapperDetails.builder()
                .mappingWrapperNameDetails(mappingWrapperNameDetails)
                .annotationParamFullNames(annotationParamFullNames)
                .interfaceMethodSignatures(interfaceMethodSignatureSet)
                .sourceMethodSignatures(sourceMethodSignatureSet)
                .mapperMethodSignatures(mapperMethodSignatureSet)
                .build();
    }

    private Set<MethodSignature> getMethods(Class<?> clazz) {
        return Stream.of(clazz)
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .map(m -> {
                    final String methodName = m.getName();
                    final String returnTypeName = m.getReturnType().getCanonicalName();
                    final String paramNames = Arrays.stream(m.getParameterTypes())
                            .map(Class::getCanonicalName)
                            .findFirst().orElse(null);
                    return new MethodSignature(methodName, returnTypeName, paramNames);
                })
                .collect(Collectors.toSet());
    }

    private MethodSignature extractMethodDetails(Method method) {
        final String returnTypeName = method.getReturnType().getCanonicalName();
        final String paramNames = Stream.of(method)
                .map(Method::getParameterTypes)
                .flatMap(Arrays::stream)
                .map(Class::getCanonicalName)
                .findFirst().orElse(null);
        return new MethodSignature(method.getName(), returnTypeName, paramNames);
    }
}
