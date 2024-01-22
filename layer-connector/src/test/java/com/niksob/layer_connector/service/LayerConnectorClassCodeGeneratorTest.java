package com.niksob.layer_connector.service;

import com.niksob.layer_connector.config.model.class_details.InterfaceClassDetailsTestConfig;
import com.niksob.layer_connector.config.model.class_details.MapperClassDetailsTestConfig;
import com.niksob.layer_connector.config.model.class_details.SourceClassDetailsTestConfig;
import com.niksob.layer_connector.logger.Logger;
import com.niksob.layer_connector.MainContextTest;
import com.niksob.layer_connector.model.LayerConnectorClassCode;
import com.niksob.layer_connector.model.class_details.LayerConnectorClassDetails;
import com.niksob.layer_connector.model.compiler.CompilationDetails;
import com.niksob.layer_connector.parser.LayerConnectorClassParser;
import com.niksob.layer_connector.service.generation.code.layer_connector.clazz.LayerConnectorCodeGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LayerConnectorClassCodeGeneratorTest extends MainContextTest {
    @Autowired
    private LayerConnectorCodeGenerator layerConnectorCodeGenerator;

    @MockBean
    private Logger log;

    @Autowired
    private InterfaceClassDetailsTestConfig interfaceClassConfig;
    @Autowired
    private SourceClassDetailsTestConfig sourceClassConfig;
    @Autowired
    private MapperClassDetailsTestConfig mapperClassConfig;
    @Autowired
    private CompilationDetails compilationDetails;

    private LayerConnectorClassCode readLayerConnectorExample(String filePath) {
        final LayerConnectorClassParser classParser = new LayerConnectorClassParser();

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
        var layerConnectorClassDetails = new LayerConnectorClassDetails(
                interfaceClassConfig.getDetails(),
                sourceClassConfig.getDetails(),
                mapperClassConfig.getDetails(),
                true,
                compilationDetails

        );
        final String classCodeStr = layerConnectorCodeGenerator.generateClassCode(layerConnectorClassDetails);
        var expectedClassCode = readLayerConnectorExample("./src/test/resources/layer_connector_class.example");

        assertClassCode(classCodeStr, expectedClassCode);
    }

    @Test
    public void testMapperWithGeneric() {
        var layerConnectorClassDetails = new LayerConnectorClassDetails(
                interfaceClassConfig.getDetailsWithGeneric(),
                sourceClassConfig.getDetailsWithGeneric(),
                mapperClassConfig.getDetailsWithGeneric(),
                true,
                compilationDetails
        );
        final String classCodeStr = layerConnectorCodeGenerator.generateClassCode(layerConnectorClassDetails);
        var expectedClassCode = readLayerConnectorExample(
                "./src/test/resources/mono_layer_connector_class.example"
        );
        assertClassCode(classCodeStr, expectedClassCode);
    }

    @Test
    public void testGeneratedClassTextWithWrongSourceClass() {
        var layerConnectorClassDetails = new LayerConnectorClassDetails(
                interfaceClassConfig.getDetails(),
                sourceClassConfig.getDetailsWithInsufficientMethods(),
                mapperClassConfig.getDetails(),
                true,
                compilationDetails
        );
        Assertions.assertThatThrownBy(() -> layerConnectorCodeGenerator.generateClassCode(layerConnectorClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("There are fewer methods declared in the source class %s than in the LayerConnector interface %s. Remove unnecessary methods in the LayerConnector or add-ons to the source class"
                        .formatted(
                                sourceClassConfig.getDetails().getName(),
                                interfaceClassConfig.getDetails().getName()
                        ));
    }

    @Test
    public void testWithMapperWithIncompleteMethodsForConverting() {
        var layerConnectorClassDetails = new LayerConnectorClassDetails(
                interfaceClassConfig.getDetails(),
                sourceClassConfig.getDetails(),
                mapperClassConfig.getIncompleteDetails(),
                true,
                compilationDetails
        );
        Assertions.assertThatThrownBy(() -> layerConnectorCodeGenerator.generateClassCode(layerConnectorClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Mappers needed for LayerConnector interface com.niksob.layer_connector.dao.user.UserDao does not have a method for mapping all values");
    }

    @Test
    public void testWithSeveralIdenticalMapperReturnTypes() {
        var layerConnectorClassDetails = new LayerConnectorClassDetails(
                interfaceClassConfig.getDetails(),
                sourceClassConfig.getDetails(),
                mapperClassConfig.getDetailsWithIdenticalReturnTypes(),
                true,
                compilationDetails
        );
        Assertions.assertThatThrownBy(() -> layerConnectorCodeGenerator.generateClassCode(layerConnectorClassDetails))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The mapper com.niksob.layer_connector.mapper.user.UserEntityMapper has duplicates among the conversion methods");
    }

    private void assertClassCode(String classCodeStr, LayerConnectorClassCode expectedClassCode) {
        //Package name assertion
        Assertions.assertThat(classCodeStr.contains(expectedClassCode.getPackageName()))
                .withFailMessage(String.format("Class code has wrong package name.\nExpected:\n%s\nActual full class code:\n%s",
                        expectedClassCode.getPackageName(), classCodeStr
                )).isTrue();

        // Generated annotation assertion
        Assertions.assertThat(classCodeStr.contains(expectedClassCode.getGeneratedAnnotation()))
                .withFailMessage(String.format("Class code hasn't spring generation annotation.\nExpected:\n%s\nActual full class code:\n%s",
                        expectedClassCode.getGeneratedAnnotation(), classCodeStr
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
