package com.niksob.layer_connector.parser;

import com.niksob.layer_connector.model.LayerConnectorClassCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class LayerConnectorClassParser {
    public LayerConnectorClassCode parse(String classCode) {
        var classCodeStrBuilder = new StringBuilder(classCode);
        LayerConnectorClassCode layerConnectorClassCode = new LayerConnectorClassCode();
        final String methodEndMarker = "\n\n";
        final String lineEndMarker = "\n";

        //Package line boundaries
        String packageLine = cutStringBetween(classCodeStrBuilder, "package", lineEndMarker);
        removeEmptyLines(classCodeStrBuilder);

        //Generated annotation
        final String endOfGeneratedAnnotation = ")";
        String generatedAnnotationLines = cutStringBetween(
                classCodeStrBuilder, "@javax.annotation.processing.Generated(", endOfGeneratedAnnotation
        ) + endOfGeneratedAnnotation;
        removeEmptyLines(classCodeStrBuilder);

        //Component annotation
        String componentAnnotationLine = cutStringBetween(classCodeStrBuilder, "@org.springframework.stereotype.Component", lineEndMarker);
        removeEmptyLines(classCodeStrBuilder);

        //Class name
        String classNameLine = cutStringBetween(classCodeStrBuilder, "public class", lineEndMarker);
        removeEmptyLines(classCodeStrBuilder);

        //Fields
        var fields = new ArrayList<String>();
        final String fieldMarker = "private final";
        while (contains(classCodeStrBuilder, fieldMarker)) {
            String fieldLine = cutStringBetween(classCodeStrBuilder, fieldMarker, lineEndMarker);
            fields.add(fieldLine);
            removeEmptyLines(classCodeStrBuilder);
        }

        //Constructor
        String constructorLines = cutStringBetween(classCodeStrBuilder, "public UserDaoMappingWrapper(", methodEndMarker);
        removeEmptyLines(classCodeStrBuilder);

        //Methods
        var methods = new ArrayList<String>();
        final String methodMarker = "@Override";
        String methodLines;
        while (contains(classCodeStrBuilder, methodMarker)) {
            methodLines = cutStringBetween(classCodeStrBuilder, methodMarker, methodEndMarker);
            methods.add(methodLines);
            removeEmptyLines(classCodeStrBuilder);
        }

        return layerConnectorClassCode.setPackageName(packageLine)
                .setGeneratedAnnotation(generatedAnnotationLines)
                .setComponentAnnotation(componentAnnotationLine)
                .setClassName(classNameLine)
                .setFields(fields)
                .setConstructor(constructorLines)
                .setMethods(methods)
                .setEndChar("}");
    }

    public String cutStringBetween(StringBuilder stringBuilder, String beginMarker, String endMarker) {
        final int begin = stringBuilder.indexOf(beginMarker);
        final int end = stringBuilder.indexOf(endMarker);
        final String substring = stringBuilder.substring(begin, end);
        stringBuilder.replace(begin, end, "");
        return substring;
    }

    public void removeEmptyLines(StringBuilder stringBuilder) {
        for (int i = 0; i < stringBuilder.length(); i++) {
            final char sym = stringBuilder.charAt(i);
            if (sym == '\n') {
                continue;
            } else if (sym == ' ') {
                continue;
            }
            if (i != 0) {
                stringBuilder.replace(0, i, "");
                break;
            }
        }
    }

    public boolean contains(StringBuilder stringBuilder, String substring) {
        final int i = stringBuilder.indexOf(substring);
        return i != -1;
    }
}
