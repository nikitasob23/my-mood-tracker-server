package com.niksob.mapping_wrapper.service.element;

import com.niksob.mapping_wrapper.model.executable_element.MethodSignature;
import com.niksob.mapping_wrapper.model.mapping_wrapper.marker.Marker;
import lombok.AllArgsConstructor;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class ElementMethodServiceImpl implements ElementMethodService {
    private final Set<String> objectStandardMethodNames = Stream.of(Object.class)
            .map(Class::getDeclaredMethods)
            .flatMap(Arrays::stream)
            .map(Method::getName)
            .collect(Collectors.toSet());

    @Override
    public Set<MethodSignature> extractSignature(Element element, Marker marker) {
        return Stream.of(element)
                .map(this::filterUniqueExecutable)
                .flatMap(Collection::stream)
                .map(this::extractSignature)
                .collect(Collectors.toSet());
    }

    @Override
    public MethodSignature extractSignature(ExecutableElement e) {
        final String methodName = e.getSimpleName().toString();
        final String returnTypeName = e.getReturnType().toString();
        final String paramNames = e.getParameters().stream()
                .map(VariableElement::asType)
                .map(TypeMirror::toString)
                .findFirst().orElse(null);
        return new MethodSignature(methodName, returnTypeName, paramNames);
    }

    private Set<ExecutableElement> filterUniqueExecutable(Element element) {
        return Stream.of(element)
                .map(Element::getEnclosedElements)
                .flatMap(Collection::stream)
                .filter(this::isExecutable)
                .map(e -> (ExecutableElement) e)
                .filter(this::isNotStandardObjectMethod)
                .collect(Collectors.toSet());
    }

    private boolean isNotStandardObjectMethod(ExecutableElement e) {
        return Stream.of(e)
                .map(ExecutableElement::getSimpleName)
                .map(Name::toString)
                .noneMatch(objectStandardMethodNames::contains);
    }

    private boolean isExecutable(Element element) {
        return Stream.of(element)
                .map(Element::getKind)
                .allMatch(ElementKind.METHOD::equals);
    }
}
