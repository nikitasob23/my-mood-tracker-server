package com.niksob.layer_connector.service.element;

import com.niksob.layer_connector.logger.Logger;
import com.niksob.layer_connector.model.method_details.MethodSignature;
import com.niksob.layer_connector.model.class_details.Marker;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ElementMethodServiceImpl implements ElementMethodService {
    private final Logger log;

    private final Set<String> objectStandardMethodNames = Stream.of(Object.class)
            .map(Class::getDeclaredMethods)
            .flatMap(Arrays::stream)
            .map(Method::getName)
            .collect(Collectors.toSet());

    @Override
    public Set<MethodSignature> extractSignature(Element element, Marker marker) {
        final Set<MethodSignature> methodSignatures = Stream.of(element)
                .map(this::filterUniqueExecutable)
                .flatMap(Collection::stream)
                .map(this::extractSignature)
                .collect(Collectors.toSet());
        log.warn("%s method signatures was extracted. ObjectState = %s".formatted(marker.name(), methodSignatures));
        return methodSignatures;
    }

    @Override
    public boolean filterMethodSignaturesByNames(
            MethodSignature source, Set<MethodSignature> filterParam
    ) {
        final Set<String> names = filterParam.stream()
                .map(MethodSignature::getMethodName)
                .collect(Collectors.toSet());
        return names.contains(source.getMethodName());
    }

    private MethodSignature extractSignature(ExecutableElement e) {
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