package com.niksob.mapping_wrapper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niksob.mapping_wrapper.logger.Logger;
import com.niksob.mapping_wrapper.MainContextTest;
import com.niksob.mapping_wrapper.model.ClassCodeExample;
import com.niksob.mapping_wrapper.model.method_details.MethodSignature;
import com.niksob.mapping_wrapper.model.class_details.ClassDetails;
import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.service.code_generation.class_code.MappingWrapperCodeGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class MappingWrapperClassCodeGeneratorTest extends MainContextTest {
    private static ClassCodeExample CLASS_CODE_EXAMPLE;

    @Autowired
    private MappingWrapperCodeGenerator mappingWrapperCodeGenerator;

    @MockBean
    private Logger log;

    private final ClassDetails interfaceDetails = new ClassDetails(
            "com.niksob.mapping_wrapper.dao.user.UserDao",
            Set.of(
                    new MethodSignature("load", "com.niksob.domain.model.user.UserInfo", "com.niksob.domain.model.user.Username"),
                    new MethodSignature("save", "void", "com.niksob.domain.model.user.UserInfo"),
                    new MethodSignature("update", "com.niksob.domain.model.user.UserInfo", "com.niksob.domain.model.user.UserInfo"),
                    new MethodSignature("delete", "com.niksob.domain.model.user.UserInfo", "com.niksob.domain.model.user.Username"),
                    new MethodSignature("get", "java.lang.String", "java.lang.String"),
                    new MethodSignature("getCurrentUser", "com.niksob.domain.model.user.UserInfo", null)
            ));

    private final ClassDetails sourceDetails = new ClassDetails(
            "com.niksob.mapping_wrapper.dao.user.UserEntityDaoImpl",
            Set.of(
                    new MethodSignature("load", "com.niksob.mapping_wrapper.entity.user.UserEntity", "java.lang.String"),
                    new MethodSignature("save", "void", "com.niksob.mapping_wrapper.entity.user.UserEntity"),
                    new MethodSignature("update", "com.niksob.mapping_wrapper.entity.user.UserEntity", "com.niksob.mapping_wrapper.entity.user.UserEntity"),
                    new MethodSignature("delete", "com.niksob.mapping_wrapper.entity.user.UserEntity", "java.lang.String"),
                    new MethodSignature("getCurrentUser", "com.niksob.mapping_wrapper.entity.user.UserEntity", null),
                    new MethodSignature("clearCache", "void", null),
                    new MethodSignature("get", "java.lang.String", "java.lang.String"),
                    new MethodSignature("createEntityNotFoundException", "RuntimeException", "java.lang.String")
            ));

    private final ClassDetails mapperDetails = new ClassDetails(
            "com.niksob.mapping_wrapper.mapper.user.UserEntityMapper",
            Set.of(
                    new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"),
                    new MethodSignature("toEntityUsername", "com.niksob.domain.model.user.Username", "java.lang.String"),
                    new MethodSignature("toEntity", "com.niksob.mapping_wrapper.entity.user.UserEntity", "com.niksob.domain.model.user.UserInfo"),
                    new MethodSignature("fromEntity", "com.niksob.domain.model.user.UserInfo", "com.niksob.mapping_wrapper.entity.user.UserEntity")
            ));
    private final ClassDetails wrongSourceDetails = new ClassDetails(
            "com.niksob.mapping_wrapper.dao.user.UserEntityDaoImpl",
            Set.of(new MethodSignature("getValue", "java.lang.String", null))
    );
    private final ClassDetails incompleteMapperDetails = new ClassDetails(
            "com.niksob.mapping_wrapper.mapper.user.UserEntityMapper",
            Set.of(new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"))
    );

    private final ClassDetails mapperWithIdenticalReturnTypes = new ClassDetails(
            "com.niksob.mapping_wrapper.mapper.user.UserEntityMapper",
            Set.of(
                    new MethodSignature("toEntityUsername", "java.lang.String", "com.niksob.domain.model.user.Username"),
                    new MethodSignature("toEntityUsername2", "java.lang.String", "com.niksob.domain.model.user.Username"),
                    new MethodSignature("toEntityUsername", "com.niksob.domain.model.user.Username", "java.lang.String"),
                    new MethodSignature("toEntity", "com.niksob.mapping_wrapper.entity.user.UserEntity", "com.niksob.domain.model.user.UserInfo"),
                    new MethodSignature("fromEntity", "com.niksob.domain.model.user.UserInfo", "com.niksob.mapping_wrapper.entity.user.UserEntity")
            ));

    @BeforeAll
    public static void readExampleMappingWrapperClassCodeExamples() {
        String jsonMappingWrapperCodeFilePath = "./src/test/resources/mapping_wrapper_class.example";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            CLASS_CODE_EXAMPLE = objectMapper.readValue(
                    new File(jsonMappingWrapperCodeFilePath), ClassCodeExample.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClassCodeGeneration() {
        var mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceDetails, sourceDetails, mapperDetails, true
        );
        final java.lang.String classCode = mappingWrapperCodeGenerator.generateClassCode(mappingWrapperClassDetails);

        assertThat(classCode, containsString(CLASS_CODE_EXAMPLE.getClassBeginning()));
        CLASS_CODE_EXAMPLE.getMethods().forEach(methodExample ->
                assertThat(classCode, containsString(methodExample))
        );
        assertThat(classCode, containsString(CLASS_CODE_EXAMPLE.getClassEnding()));
    }

    @Test
    public void testGeneratedClassTextWithWrongSourceClass() {
        var mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceDetails, wrongSourceDetails, mapperDetails, true
        );
        Assertions.assertThatThrownBy(() -> mappingWrapperCodeGenerator.generateClassCode(mappingWrapperClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("There are fewer methods declared in the source class %s than in the MappingWrapper interface %s. Remove unnecessary methods in the MappingWrapper or add-ons to the source class"
                        .formatted(
                                sourceDetails.getName(),
                                interfaceDetails.getName()
                        ));
    }

    @Test
    public void testWithMapperWithIncompleteMethodsForConverting() {
        var mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceDetails, sourceDetails, incompleteMapperDetails, true
        );
        Assertions.assertThatThrownBy(() -> mappingWrapperCodeGenerator.generateClassCode(mappingWrapperClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Mapper com.niksob.mapping_wrapper.mapper.user.UserEntityMapper does not have a method for mapping all values");
    }

    @Test
    public void testWithSeveralIdenticalMapperReturnTypes() {
        var mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceDetails, sourceDetails, mapperWithIdenticalReturnTypes, true
        );
        Assertions.assertThatThrownBy(() -> mappingWrapperCodeGenerator.generateClassCode(mappingWrapperClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The mapper com.niksob.mapping_wrapper.mapper.user.UserEntityMapper has duplicates among the conversion methods");
    }
}
