package com.niksob.mapping_wrapper.service;

import com.niksob.mapping_wrapper.config.model.class_details.InterfaceClassDetailsTestConfig;
import com.niksob.mapping_wrapper.config.model.class_details.MapperClassDetailsTestConfig;
import com.niksob.mapping_wrapper.config.model.class_details.SourceClassDetailsTestConfig;
import com.niksob.mapping_wrapper.logger.Logger;
import com.niksob.mapping_wrapper.MainContextTest;
import com.niksob.mapping_wrapper.model.MappingWrapperClassCode;
import com.niksob.mapping_wrapper.model.class_details.MappingWrapperClassDetails;
import com.niksob.mapping_wrapper.service.code_generation.class_code.MappingWrapperCodeGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MappingWrapperClassCodeGeneratorTest extends MainContextTest {
    @Autowired
    private MappingWrapperCodeGenerator mappingWrapperCodeGenerator;

    @MockBean
    private Logger log;

    @Autowired
    private InterfaceClassDetailsTestConfig interfaceClassConfig;
    @Autowired
    private SourceClassDetailsTestConfig sourceClassConfig;
    @Autowired
    private MapperClassDetailsTestConfig mapperClassConfig;

    private MappingWrapperClassCode readMappingWrapperExample(String filePath) {
        final MappingWrapperClassParser classParser = new MappingWrapperClassParser();

        String classCodeStr;
        try {
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            classCodeStr = new String(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classParser.parse(classCodeStr);
    }

    @Test
    public void testClassCodeGeneration() {
        var mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceClassConfig.getDetails(),
                sourceClassConfig.getDetails(),
                mapperClassConfig.getDetails(),
                true
        );
        final String classCodeStr = mappingWrapperCodeGenerator.generateClassCode(mappingWrapperClassDetails);
        var expectedClassCode = readMappingWrapperExample("./src/test/resources/mapping_wrapper_class.example");

        assertClassCode(classCodeStr, expectedClassCode);
    }

    @Test
    public void testMapperWithGeneric() {
        var mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceClassConfig.getDetailsWithGeneric(),
                sourceClassConfig.getDetailsWithGeneric(),
                mapperClassConfig.getDetailsWithGeneric(),
                true
        );
        final String classCodeStr = mappingWrapperCodeGenerator.generateClassCode(mappingWrapperClassDetails);
        var expectedClassCode = readMappingWrapperExample(
                "./src/test/resources/mono_mapping_wrapper_class.example"
        );
        assertClassCode(classCodeStr, expectedClassCode);
    }

    @Test
    public void testGeneratedClassTextWithWrongSourceClass() {
        var mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceClassConfig.getDetails(),
                sourceClassConfig.getDetailsWithInsufficientMethods(),
                mapperClassConfig.getDetails(),
                true
        );
        Assertions.assertThatThrownBy(() -> mappingWrapperCodeGenerator.generateClassCode(mappingWrapperClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("There are fewer methods declared in the source class %s than in the MappingWrapper interface %s. Remove unnecessary methods in the MappingWrapper or add-ons to the source class"
                        .formatted(
                                sourceClassConfig.getDetails().getName(),
                                interfaceClassConfig.getDetails().getName()
                        ));
    }

    @Test
    public void testWithMapperWithIncompleteMethodsForConverting() {
        var mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceClassConfig.getDetails(),
                sourceClassConfig.getDetails(),
                mapperClassConfig.getIncompleteDetails(),
                true
        );
        Assertions.assertThatThrownBy(() -> mappingWrapperCodeGenerator.generateClassCode(mappingWrapperClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Mappers needed for MappingWrapper interface com.niksob.mapping_wrapper.dao.user.UserDao does not have a method for mapping all values");
    }

    @Test
    public void testWithSeveralIdenticalMapperReturnTypes() {
        var mappingWrapperClassDetails = new MappingWrapperClassDetails(
                interfaceClassConfig.getDetails(), sourceClassConfig.getDetails(), mapperClassConfig.getDetailsWithIdenticalReturnTypes(), true
        );
        Assertions.assertThatThrownBy(() -> mappingWrapperCodeGenerator.generateClassCode(mappingWrapperClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The mapper com.niksob.mapping_wrapper.mapper.user.UserEntityMapper has duplicates among the conversion methods");
    }

    private void assertClassCode(String classCodeStr, MappingWrapperClassCode expectedClassCode) {
        //Package name assertion
        Assertions.assertThat(classCodeStr.contains(expectedClassCode.getPackageName()))
                .withFailMessage(String.format("Class code has wrong package name.\nExpected:\n%s\nActual full class code:\n%s",
                        expectedClassCode.getPackageName(), classCodeStr
                )).isTrue();

        //Spring component annotation assertion
        Assertions.assertThat(classCodeStr.contains(expectedClassCode.getComponentAnnotation()))
                .withFailMessage(String.format("Class code hasn't spring component annotation.\nExpected:\n%s\nActual full class code:\n%s",
                        expectedClassCode.getComponentAnnotation(), classCodeStr
                )).isTrue();

        //Class name assertion
        Assertions.assertThat(classCodeStr.contains(expectedClassCode.getClassName()))
                .withFailMessage(String.format("Class code has wrong class name.\nExpected:\n%s\nActual full class code:\n%s",
                        expectedClassCode.getClassName(), classCodeStr
                )).isTrue();

        //Fields assertion
        expectedClassCode.getFields().forEach(field ->
                Assertions.assertThat(classCodeStr.contains(field))
                        .withFailMessage(String.format("Class code has wrong field.\nExpected:\n%s\nActual full class code:\n%s",
                                field, classCodeStr
                        )).isTrue()
        );

        //Constructor assertion
        Assertions.assertThat(classCodeStr.contains(expectedClassCode.getConstructor()))
                .withFailMessage(String.format("Class code has wrong constructor.\nExpected:\n%s\nActual full class code:\n%s",
                        expectedClassCode.getConstructor(), classCodeStr
                )).isTrue();

        //Methods assertion
        expectedClassCode.getMethods().forEach(method ->
                Assertions.assertThat(classCodeStr.contains(method))
                        .withFailMessage(String.format("Class code has wrong method.\nExpected:\n%s\nActual full class code:\n%s",
                                method, classCodeStr
                        )).isTrue()
        );

        //End class sym assertion
        Assertions.assertThat(classCodeStr.charAt(classCodeStr.length() - 1))
                .withFailMessage("Class code hasn't end class symbol: }")
                .isEqualTo('}');
    }
}
