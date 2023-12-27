package com.niksob.mapping_wrapper.util;

import com.niksob.mapping_wrapper.model.method_details.MethodSignature;
import com.niksob.mapping_wrapper.model.method_details.VoidReturnType;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@NoArgsConstructor
public class ClassUtilImpl implements ClassUtil {
    private static final String SPLIT_CLASS_NAME_SYM = ".";

    @Override
    public boolean compareParams(MethodSignature method1, MethodSignature method2) {
        return method1.getParamType().equals(method2.getParamType());
    }

    @Override
    public boolean compareReturnTypes(MethodSignature method1, MethodSignature method2) {
        return method1.getReturnType().equals(method2.getReturnType());
    }

    @Override
    public boolean haveParam(MethodSignature method) {
        return method.getParamType() != null;
    }

    @Override
    public boolean returnVoid(MethodSignature method) {
        return method.getReturnType().equals(VoidReturnType.VOID.getValue())
                || returnVoidObject(method.getReturnType());
    }

    @Override
    public String getShortClassName(String name) {
        return Stream.of(name.lastIndexOf(SPLIT_CLASS_NAME_SYM))
                .filter(this::symExists)
                .map(i -> name.substring(i + 1))
                .findFirst().orElseThrow(() -> new IllegalStateException("The short name of the class was not found"));
    }

    @Override
    public String stickNames(String name1, String name2) {
        return name1 + name2;
    }

    @Override
    public String getPackageName(String name) {
        return Stream.of(name.lastIndexOf(SPLIT_CLASS_NAME_SYM))
                .filter(this::symExists)
                .map(i -> name.substring(0, i))
                .findFirst().orElseThrow(() -> new IllegalStateException("The package name of the class was not found"));
    }

    private boolean returnVoidObject(String returnType) {
        return returnType.contains(VoidReturnType.GENERIC_PARAM.getValue())
                || returnType.equals(VoidReturnType.VOID_OBJECT.getValue());
    }

    private boolean symExists(Integer symNum) {
        return symNum != -1;
    }
}
