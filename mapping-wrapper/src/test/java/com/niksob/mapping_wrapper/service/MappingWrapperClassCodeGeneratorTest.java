package com.niksob.mapping_wrapper.service;

import com.niksob.domain.model.user.Nickname;
import com.niksob.mapping_wrapper.Logger;
import com.niksob.mapping_wrapper.MainContextTest;
import com.niksob.mapping_wrapper.model.*;
import com.niksob.mapping_wrapper.model.executable_element.MethodSignature;
import com.niksob.mapping_wrapper.model.mapping_wrapper.*;
import com.niksob.mapping_wrapper.service.code_generation.class_code.GenerateMappingWrapperCodeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

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
    private GenerateMappingWrapperCodeService generateMappingWrapperCodeService;

    @MockBean
    private Logger log;

    private MappingWrapperClassDetails mappingWrapperClassDetails;

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
                /*sourceClass = */UserEntityDaoImpl.class,
                /*mapperClass = */UserEntityMapper.class
        );
        final String classCode = generateMappingWrapperCodeService.generateClassCode(mappingWrapperClassDetails);
        assertThat(classCode).isEqualTo(MAPPING_WRAPPER_CLASS_EXAMPLE);
    }

    @Test
    public void testGeneratedClassTextWithWrongSourceClass() {
        prepare(
                /*sourceClass = */Nickname.class,
                /*mapperClass = */UserEntityMapper.class
        );
        Assertions.assertThatThrownBy(() -> generateMappingWrapperCodeService.generateClassCode(mappingWrapperClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Didn't find source method to mapping parameter: com.niksob.domain.model.user.Username of com.niksob.domain.model.user.Nickname class's method load");
    }

    @Test
    public void testWithMapperWithIncompletedMethodsForConverting() {
        prepare(
                /*sourceClass = */UserEntityDaoImpl.class,
                /*mapperClass = */TestWrongUserMapper.class
        );
        Assertions.assertThatThrownBy(() -> generateMappingWrapperCodeService.generateClassCode(mappingWrapperClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Mapper %s does not have a method for mapping all values"
                        .formatted(TestWrongUserMapper.class.getCanonicalName()));
    }

    @Test
    public void testWithSeveralIdenticalMapperReturnTypes() {
        prepare(
                /*sourceClass = */UserEntityDaoImpl.class,
                /*mapperClass = */TestUserMapperWithSeveralReturnTypes.class
        );
        Assertions.assertThatThrownBy(() -> generateMappingWrapperCodeService.generateClassCode(mappingWrapperClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The mapper %s has duplicates among the conversion methods"
                        .formatted(TestUserMapperWithSeveralReturnTypes.class.getCanonicalName()));
    }

    private void prepare(Class<?> sourceClass, Class<?> mapperClass) {
        final Class<?> interfaceClass = UserDao.class;

        var interfaceClassDetails = new ClassDetails(
                interfaceClass.getCanonicalName(),
                getMethods(interfaceClass)
        );

        var sourceClassDetails = new ClassDetails(
                sourceClass.getCanonicalName(),
                getMethods(sourceClass).stream()
                        .filter(methodDetails -> interfaceClassDetails.getMethods().stream()
                                .map(MethodSignature::getMethodName)
                                .anyMatch(methodDetails.getMethodName()::equals)
                        )
                        .collect(Collectors.toSet()));

        var mapperClassDetails = new ClassDetails(
                mapperClass.getCanonicalName(),
                Stream.of(mapperClass)
                        .map(Class::getDeclaredMethods)
                        .flatMap(Arrays::stream)
                        .map(this::extractMethodDetails)
                        .collect(Collectors.toSet()));

        mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceClassDetails, sourceClassDetails, mapperClassDetails
        );
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
